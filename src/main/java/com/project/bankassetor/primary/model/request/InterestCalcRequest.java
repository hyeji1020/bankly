package com.project.bankassetor.primary.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class InterestCalcRequest {

    private BigDecimal monthlyAmount;

    @Builder
    public InterestCalcRequest(BigDecimal monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

}
