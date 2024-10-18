package com.project.bankassetor.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountTransferRequest {
//
//    @NotNull(message = "출금 하실 계좌 번호 입력은 필수 입니다.")
//    Long withdrawalNumber;

    @NotNull(message = "이체 하실 계좌 번호 입력은 필수 입니다.")
    Long toAccountNumber;

    @NotNull(message = "이체 금액 입력은 필수 입니다.")
    @Min(0)
    int amount;

    @Builder
    public AccountTransferRequest(Long toAccountNumber, int amount) {
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

}
