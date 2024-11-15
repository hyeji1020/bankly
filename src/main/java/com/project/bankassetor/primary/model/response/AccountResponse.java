package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class AccountResponse {

    private long accountId;

    // 입출금 계좌 번호
    private String accountNumber;

    // 입출금 후 잔고
    private String balance;

    // 생성일자
    private String createdAt;

    private static String formatBalance(BigDecimal balance) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 소수점 둘째 자리까지 표시
        return decimalFormat.format(balance);
    }

    private static String formatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static AccountResponse of(Account account){
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(formatBalance(account.getBalance()))
                .createdAt(formatter(account.getCreatedAt()))
                .build();
    }

    public static List<AccountResponse> of(List<Account> accounts){
        return accounts.stream()
                .map(account -> AccountResponse.builder()
                        .accountId(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .balance(formatBalance(account.getBalance()))
                        .createdAt(formatter(account.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

}
