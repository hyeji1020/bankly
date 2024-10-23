package com.project.bankassetor.service.perist;

import com.project.bankassetor.listener.AccessLogListener;
import com.project.bankassetor.model.entity.AccessLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogListener accessLogListener;

    public void save(AccessLog accessLog) {
        accessLogListener.receiveAccessLog(accessLog);
    }
}
