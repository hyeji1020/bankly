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
public class AccountCreateResponse {

    private String accountType;
    private String accountNumber;
    private String balance;

    private static String formatBalance(BigDecimal balance) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 소수점 둘째 자리까지 표시
        return decimalFormat.format(balance);
    }

    public static AccountCreateResponse of(Account account){
        return AccountCreateResponse.builder()
                .accountType(account.getType().toString())
                .accountNumber(account.getAccountNumber())
                .balance(formatBalance(account.getBalance()))
                .build();
    }
}
