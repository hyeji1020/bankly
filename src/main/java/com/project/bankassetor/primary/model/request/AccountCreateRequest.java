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

    @Builder
    public AccountCreateRequest(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}
