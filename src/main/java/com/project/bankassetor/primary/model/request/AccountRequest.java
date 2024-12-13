package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {

    @NotBlank(message = "계좌 번호 입력은 필수 입니다.")
    String accountNumber;

    @NotNull(message = "입출금 금액 입력은 필수 입니다.")
    @Min(0)
    BigDecimal amount;

    @Builder
    public AccountRequest(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

}
