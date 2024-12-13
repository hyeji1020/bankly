package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class AccountCreateRequest {

    // 초기 입금액
    @Min(0)
    BigDecimal initialDeposit;

    // 월 입금액
    @Min(0)
    BigDecimal monthlyDeposit;

    // 입금 한도
    BigDecimal depositLimit;

    @Builder
    public AccountCreateRequest(BigDecimal initialDeposit, BigDecimal monthlyDeposit, BigDecimal depositLimit) {
        this.initialDeposit = initialDeposit;
        this.monthlyDeposit = monthlyDeposit;
        this.depositLimit = depositLimit;
    }

}
