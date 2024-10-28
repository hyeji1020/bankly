package com.project.bankassetor.service.perist;

import com.project.bankassetor.secondary.model.entity.AccessLog;
import com.project.bankassetor.secondary.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public void saveBatch(ArrayList<AccessLog> accessLogs) {
        accessLogRepository.saveAll(accessLogs);
    }
}
