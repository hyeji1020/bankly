package com.project.bankassetor.model.response;

import com.project.bankassetor.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AccountResponse {

    // 입출금 계좌 번호
    private Long accountNumber;

    // 입출금 후 잔고
    private int balance;

    public static AccountResponse of(Account account){
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

}
