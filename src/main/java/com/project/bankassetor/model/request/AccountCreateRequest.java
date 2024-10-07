package com.project.bankassetor.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountCreateRequest {

    @NotBlank(message = "생성하실 계좌 유형을 선택해주세요. (예: SAVING, CHECKING)")
    String accountType;

    // 초기 입금액
    int initialDeposit;

    @Builder
    public AccountCreateRequest(String accountType, int initialDeposit) {
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
    }
}
