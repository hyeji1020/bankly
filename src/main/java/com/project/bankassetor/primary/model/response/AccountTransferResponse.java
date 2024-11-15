package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@AllArgsConstructor
@Builder
@Getter
public class AccountTransferResponse {

    // 이체 상대 이름
    private long userId;

    // 이체 한 계좌 번호
    private long toAccountId;

    // 이체 금액
    private String amount;

    private static String formatAmount(BigDecimal amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    public static AccountTransferResponse of(BankAccount bankAccount, BigDecimal amount){
        return AccountTransferResponse.builder()
                .userId(bankAccount.getMemberId())
                .toAccountId(bankAccount.getCheckingAccountId())
                .amount(formatAmount(amount))
                .build();
    }

}
