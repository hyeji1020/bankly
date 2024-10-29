package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class AccountCreateResponse {

    // 계좌 유형 (예: SAVING, CHECKING)
    private String accountType;
    private String accountNumber;
    private BigDecimal balance;

    public static AccountCreateResponse of(Account account){
        return AccountCreateResponse.builder()
                .accountType(account.getAccountType().toString())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }
}
