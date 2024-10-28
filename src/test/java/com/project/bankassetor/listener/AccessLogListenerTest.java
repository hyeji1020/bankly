package com.project.bankassetor.listener;

import com.project.bankassetor.secondary.model.entity.AccessLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 테스트가 끝날 때마다 컨텍스트 초기화
class AccessLogListenerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String queueName = "accessLogQueue";

    @BeforeEach
    public void setUp(){
        rabbitTemplate.execute(channel -> {
            channel.queuePurge(queueName);  // 큐의 모든 메시지 삭제
            return null;
        });
    }

    @DisplayName("50개의 메세지 accesLogQueue로 전송 성공")
    @Test
    void testMqConsumerProcessesAllMessages() {
        int totalMessages = 50;
        for (int i = 0; i < totalMessages; i++) {
            AccessLog accessLog = new AccessLog("로그 " + i, "INFO", "로그 메시지 " + i);
            rabbitTemplate.convertAndSend(queueName, accessLog);
        }
    }
}