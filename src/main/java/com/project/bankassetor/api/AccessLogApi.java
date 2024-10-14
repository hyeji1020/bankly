package com.project.bankassetor.api;

import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.service.perist.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("access")
@RestController
@RequestMapping("/api/access-log")
@RequiredArgsConstructor
public class AccessLogApi {

    private final AccessLogService accessLogService;

    @PostMapping
    public void saveLog(@RequestBody AccessLog accessLog) {
        accessLogService.save(accessLog);
    }

}
