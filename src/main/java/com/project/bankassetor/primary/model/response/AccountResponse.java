package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class AccountResponse {

    // 입출금 계좌 번호
    private String accountNumber;

    // 입출금 후 잔고
    private BigDecimal balance;

    // 생성일자
    private LocalDateTime createdAt;

    public static AccountResponse of(Account account){
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .build();
    }

    public static List<AccountResponse> of(List<Account> accounts){
        return accounts.stream()
                .map(account -> AccountResponse.builder()
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .createdAt(account.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

}
