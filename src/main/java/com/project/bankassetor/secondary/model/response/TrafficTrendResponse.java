package com.project.bankassetor.secondary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TrafficTrendResponse {

    private String hour;
    private int count;
}
