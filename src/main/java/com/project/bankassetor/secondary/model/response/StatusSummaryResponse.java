package com.project.bankassetor.secondary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class StatusSummaryResponse {
    private String status;
    private int count;
}
