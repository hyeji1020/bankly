package com.project.bankassetor.listner;

import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.repository.AccessLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 테스트가 끝날 때마다 컨텍스트 초기화
class AccessLogListenerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AccessLogRepository accessLogRepository;

    private final String queueName = "accessLogQueue";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testMqConsumerProcessesAllMessages() {
        // 10만 개의 AccessLog 메시지를 MQ로 전송
        int totalMessages = 100000;
        IntStream.range(0, totalMessages).forEach(i -> {
            AccessLog accessLog = new AccessLog("로그 " + i, "INFO", "로그 메시지 " + i);
            rabbitTemplate.convertAndSend(queueName, accessLog);
        });

        // Awaitility로 10만 개의 메시지가 모두 DB에 저장될 때까지 대기
        await().atMost(5, TimeUnit.MINUTES)  // 최대 5분 대기
                .until(() -> accessLogRepository.count() == totalMessages);

        // 모든 메시지가 DB에 제대로 들어갔는지 검증
        assertThat(accessLogRepository.count()).isEqualTo(totalMessages);
    }

}