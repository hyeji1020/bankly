package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SavingAccountCreateRequest {

    @NotNull(message = "월 납부액 선택은 필수 입니다.")
    BigDecimal monthlyDeposit;

    @Min(0)
    BigDecimal initialDeposit;

    @Builder
    public SavingAccountCreateRequest(BigDecimal monthlyDeposit, BigDecimal initialDeposit) {
        this.monthlyDeposit = monthlyDeposit;
        this.initialDeposit = initialDeposit;
    }
}
