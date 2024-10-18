package com.project.bankassetor.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountCreateRequest {

    // 초기 입금액
    int initialDeposit;

    @Builder
    public AccountCreateRequest(int initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}
