package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@AllArgsConstructor
@Builder
@Getter
public class AccountTransferResponse {

    // 이체 한 계좌 번호
    private String toAccountNumber;

    // 이체 금액
    private String amount;

    private static String formatAmount(BigDecimal amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    public static AccountTransferResponse of(Account account, BigDecimal amount){
        return AccountTransferResponse.builder()
                .amount(formatAmount(amount))
                .toAccountNumber(account.getAccountNumber())
                .build();
    }

}
