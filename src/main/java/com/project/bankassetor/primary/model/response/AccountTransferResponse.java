package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class AccountTransferResponse {

    // 이체 상대 이름
    private long userId;

    // 이체 한 계좌 번호
    private long toAccountId;

    // 이체 금액
    private BigDecimal amount;

    public static AccountTransferResponse of(BankAccount bankAccount, BigDecimal amount){
        return AccountTransferResponse.builder()
                .userId(bankAccount.getUserId())
                .toAccountId(bankAccount.getCheckingAccountId())
                .amount(amount)
                .build();
    }

}
