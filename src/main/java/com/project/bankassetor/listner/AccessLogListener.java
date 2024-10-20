package com.project.bankassetor.listner;

import com.project.bankassetor.exception.AccessLogException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessLogListener implements SmartLifecycle {

    private final AccessLogRepository accessLogRepository;
    private final List<AccessLog> accessLogBatch = new ArrayList<>();
    private volatile boolean running = false;

    /**
     * AccessLog를 직접 저장하는 메서드 (외부에서 호출 가능)
     */
    public void receiveAccessLog(AccessLog accessLog) {
        try {
            log.info("직접 호출: 전달 받은 AccessLog: {}", toJson(accessLog));
            accessLogBatch.add(accessLog);  // 배치 리스트에 로그 추가
        } catch (Exception e) {
            log.error("AccessLog 처리 중 오류 발생: {}", toJson(accessLog), e);
            throw new AccessLogException(ErrorCode.ACCESS_LOG_ERROR);
        }
    }

    /**
     * MQ 메시지 큐에서 AccessLog 메시지를 수신하는 메서드 (RabbitMQ에서 호출됨)
     */
    @RabbitListener(queues = "accessLogQueue")
    public void receiveAccessLog(AccessLog accessLog, Channel channel, Message message) {
        try {
            log.info("전달 받은 AccessLog: {}", toJson(accessLog));
            accessLogBatch.add(accessLog);  // 배치 리스트에 로그 추가 (저장은 주기적으로 처리)
        } catch (Exception e) {
            log.error("AccessLog를 처리하는 중 오류가 발생했습니다 : {}", accessLog, e);
            handleFailedMessage(channel, message);
        }
    }

    /**
     * 일정 주기로 배치에 쌓인 AccessLog 데이터를 저장하는 메서드
     * 1분마다 실행되며, 배치 리스트가 비어 있지 않을 경우 로그들을 저장
     */
    @Scheduled(fixedRate = 60000)  // 1분마다 실행 (배치 주기)
    public void saveBatch() {
        if (!accessLogBatch.isEmpty()) {  // 배치 리스트가 비어 있지 않으면 저장
            try {
                log.info("배치로 저장 중: AccessLogs 크기={}", accessLogBatch.size());
                accessLogRepository.saveAll(new ArrayList<>(accessLogBatch));
                accessLogBatch.clear();  // 저장 후 리스트 초기화
            } catch (Exception e) {
                log.error("AccessLog 배치 저장 실패", e);
                throw new AccessLogException(ErrorCode.ACCESS_LOG_BATCH_SAVE_ERROR);
            }
        }
    }

    /**
     * Dead Letter Queue로 메시지를 전송하는 메서드
     * 메시지 처리 중 예외가 발생하면 Dead Letter Queue로 보내기 위한 Nack 처리
     */
    private void handleFailedMessage(Channel channel, Message message) {
        try {
            // NACK(negative acknowledgement)로 메시지를 처리하지 않고 Dead Letter Queue로 이동시킴
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.info("Dead Letter Queue로 메시지 전송됨: {}", new String(message.getBody()));
        } catch (IOException e) {
            log.error("Dead Letter Queue로 메시지 전송 실패", e);
        }
    }

    /**
     * 애플리케이션 시작 시 리스너가 자동으로 시작되도록 설정하는 메서드
     * true를 반환하면 애플리케이션 시작 시 자동으로 리스너가 시작됨
     */
    public boolean isAutoStartup() {
        return true;  // 애플리케이션이 시작될 때 자동으로 시작
    }

    /**
     * 애플리케이션 시작 시 자동으로 호출되어 리스너를 시작하는 메서드
     * 이 메서드가 호출되면 AccessLogListener가 실행 상태로 설정됨
     */
    @Override
    public void start() {
        running = true;
        log.info("AccessLogListener 시작");
    }

    /**
     * 애플리케이션 종료 시 호출되어 리스너를 정리하는 메서드
     * Gracefully Shutdown을 위해 현재 처리 중인 배치를 끝까지 저장하고 종료
     */
    @Override
    public void stop() {
        running = false;
        log.info("AccessLogListener 종료 대기 중...");
        // 모든 로그를 처리할 때까지 대기 (현재 배치를 저장하고 비움)
        saveBatch();
    }

    /**
     * 리스너가 현재 실행 중인지 여부를 반환하는 메서드
     * 리스너의 실행 상태를 확인하기 위해 사용됨
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 리스너의 종료 순서를 결정하는 메서드
     * 이 값이 클수록 다른 컴포넌트보다 나중에 종료됨으로, 안전하게 로그 처리를 완료한 후 종료
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;  // Gracefully shutdown 순서를 제어 (값이 높을수록 나중에 종료됨)
    }
}