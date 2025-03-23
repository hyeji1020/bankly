package com.project.bankassetor.secondary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TimeSummaryResponse {
    private String uri;
    private double avgResponseTime;
    private int maxResponseTime;
}
