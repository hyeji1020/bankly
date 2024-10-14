package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.AccessLog;
import com.project.bankassetor.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLog save(AccessLog accessLog) {
        AccessLog savedAccessLog = accessLogRepository.save(accessLog);
        return savedAccessLog;
    }
}
