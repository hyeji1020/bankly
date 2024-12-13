package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class SavingAccountCreateRequest {

    @NotNull(message = "월 납입액은 필수 입력 항목입니다.")
    @Min(value = 1, message = "월 납입액은 0보다 큰 값이어야 합니다.")
    BigDecimal monthlyDeposit;

    @Min(0)
    BigDecimal initialDeposit;

    @Builder
    public SavingAccountCreateRequest(BigDecimal monthlyDeposit, BigDecimal initialDeposit) {
        this.monthlyDeposit = monthlyDeposit;
        this.initialDeposit = initialDeposit;
    }
}
