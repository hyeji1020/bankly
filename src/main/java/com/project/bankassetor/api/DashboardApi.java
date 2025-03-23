package com.project.bankassetor.api;

import com.project.bankassetor.secondary.model.response.*;
import com.project.bankassetor.service.perist.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardApi {

    private final DashboardService service;

    @GetMapping("/url-summary")
    public List<AccessLogSummaryResponse> getUrlSummary() {
        return service.getAccessSummary();
    }

    @GetMapping("/response-time")
    public List<TimeSummaryResponse> getResponseTime() {
        return service.getResponseTimeSummary();
    }

    @GetMapping("/traffic-trend")
    public List<TrafficTrendResponse> getTrafficTrend() {
        return service.getTrafficTrend();
    }

}
