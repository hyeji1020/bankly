package com.project.bankassetor.secondary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SuspiciousRefererResponse {
    private String referer;
    private String uri;
    private String status;
    private int count;
}
