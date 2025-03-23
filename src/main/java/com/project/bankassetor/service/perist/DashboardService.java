package com.project.bankassetor.service.perist;

import com.project.bankassetor.secondary.model.response.*;
import com.project.bankassetor.secondary.repository.AccessLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    private final AccessLogRepository repository;

    // URL 통계
    public List<AccessLogSummaryResponse> getAccessSummary() {
        return repository.getAccessLogSummaryRaw().stream()
                .map(row -> new AccessLogSummaryResponse(
                        row[0].toString(),  // log_date
                        row[1].toString(),  // uri
                        ((Number) row[2]).intValue(),  // total
                        ((Number) row[3]).intValue(),  // crawler
                        ((Number) row[4]).intValue()   // user
                ))
                .toList();
    }

    // 평균 응답 시간
    public List<TimeSummaryResponse> getResponseTimeSummary() {
        return repository.getResponseTimeSummaryRaw().stream()
                .map(row -> new TimeSummaryResponse(
                        row[0].toString(),  // uri
                        ((Number) row[1]).doubleValue(),  // avg
                        ((Number) row[2]).intValue()      // max
                ))
                .toList();
    }


    // 시간대별 트래픽
    public List<TrafficTrendResponse> getTrafficTrend() {
        return repository.getTrafficTrendRaw().stream()
                .map(row -> new TrafficTrendResponse(
                        String.format("%02d", ((Number) row[0]).intValue()),
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }

}
