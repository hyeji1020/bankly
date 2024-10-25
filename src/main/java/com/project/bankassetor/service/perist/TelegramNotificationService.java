package com.project.bankassetor.service.perist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramNotificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot{token}/sendMessage";

    public void sendTelegramMessage(String message) {
        String url = TELEGRAM_API_URL.replace("{token}", botToken);
        String text = message;

        // 요청 파라미터 설정
        Map<String, String> params = new HashMap<>();
        params.put("chat_id", chatId);
        params.put("text", text);

        // 텔레그램으로 메시지 전송
        restTemplate.postForObject(url, params, String.class);
    }
}
