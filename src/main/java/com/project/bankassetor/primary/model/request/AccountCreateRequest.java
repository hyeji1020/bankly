package com.project.bankassetor.primary.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class AccountCreateRequest {

    // 초기 입금액
    BigDecimal initialDeposit;

    // 입금 한도
    BigDecimal depositLimit;

    @Builder
    public AccountCreateRequest(BigDecimal initialDeposit, BigDecimal depositLimit) {
        this.initialDeposit = initialDeposit;
        this.depositLimit = depositLimit;
    }
}
