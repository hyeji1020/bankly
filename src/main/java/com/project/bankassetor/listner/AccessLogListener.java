package com.project.bankassetor.listner;

import com.project.bankassetor.exception.AccessLogException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessLogListener {

    private final AccessLogRepository accessLogRepository;
    private final List<AccessLog> accessLogBatch = new ArrayList<>();

    @RabbitListener(queues = "accessLogQueue")
    public synchronized void receiveAccessLog(AccessLog accessLog) {
        try {
            log.info("전달 받은 AccessLog: {}", toJson(accessLog));
            accessLogBatch.add(accessLog);  // 배치 리스트에 로그 추가 (저장은 주기적으로 처리)
        } catch (Exception e) {
            log.error("AccessLog를 처리하는 중 오류가 발생했습니다 : {}", accessLog, e);
            throw new AccessLogException(ErrorCode.ACCESS_LOG_ERROR);
        }
    }

    @Scheduled(fixedRate = 60000)  // 1분마다 실행 (배치 주기)
    public synchronized void saveBatch() {
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

}