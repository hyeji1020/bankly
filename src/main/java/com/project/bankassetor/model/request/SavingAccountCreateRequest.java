package com.project.bankassetor.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SavingAccountCreateRequest {

    @NotNull(message = "적금 기간 선택은 필수 입니다.")
    Long savingDurationId;

    @NotNull(message = "월 납부액 선택은 필수 입니다.")
    BigDecimal monthlyDeposit;

    @Min(0)
    int initialDeposit;

    @Builder
    public SavingAccountCreateRequest(Long savingDurationId, BigDecimal monthlyDeposit, int initialDeposit) {
        this.savingDurationId = savingDurationId;
        this.monthlyDeposit = monthlyDeposit;
        this.initialDeposit = initialDeposit;
    }
}
