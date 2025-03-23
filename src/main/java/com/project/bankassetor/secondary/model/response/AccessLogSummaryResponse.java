package com.project.bankassetor.secondary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AccessLogSummaryResponse {

    private String date;
    private String uri;
    private int totalRequests;
    private int crawlerRequests;
    private int userRequests;
}
