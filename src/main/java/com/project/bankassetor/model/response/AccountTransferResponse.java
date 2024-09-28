package com.project.bankassetor.model.response;

import com.project.bankassetor.model.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AccountTransferResponse {

    // 이체 상대 이름
    private String name;

    // 이체 한 계좌 번호
    private Long transferAccount;

    // 이체 금액
    private int amount;

    public static AccountTransferResponse of(BankAccount bankAccount, int amount){
        return AccountTransferResponse.builder()
                .name(bankAccount.getUser().getName())
                .transferAccount(bankAccount.getAccount().getAccountNumber())
                .amount(amount)
                .build();
    }

}
