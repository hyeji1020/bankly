package com.project.bankassetor.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
public class HealthCheckApi {

    @GetMapping("/health-check")
    public Object healthCheck() {
        log.info("health check !");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", "UP");
        data.put("date", LocalDateTime.now());
        return data;
    }
}
