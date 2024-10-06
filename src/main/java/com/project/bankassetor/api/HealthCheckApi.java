package com.project.bankassetor.api;

import com.project.bankassetor.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
public class HealthCheckApi {
    @GetMapping("/health-check")
    public ResultResponse<Object> healthCheck() {
        log.info("health check !");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", "UP");
        data.put("date", LocalDateTime.now());
        return new ResultResponse<>(data);
    }
    @PostMapping("/health-check")
    public ResultResponse<Object> postHealthCheck(@RequestBody Map<String, Object> requestBody) {
        log.info("health check !");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("receivedData", requestBody);
        response.put("date", LocalDateTime.now());

        return new ResultResponse<>(response);
    }
}
