package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
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
public class TransactionHistoryResponse {

    LocalDateTime transactionTime;  // 거래 시간
    BigDecimal transactionAmount;  // 거래 금액
    BigDecimal balanceAfter;  // 거래 후 잔액

    public static List<TransactionHistoryResponse> of(List<CheckingTransactionHistory> transactionHistories) {
        return transactionHistories.stream()
                .map(checkingTransactionHistory -> TransactionHistoryResponse.builder()
                        .transactionTime(checkingTransactionHistory.getTransactionTime())
                        .transactionAmount(checkingTransactionHistory.getTransactionAmount())
                        .balanceAfter(checkingTransactionHistory.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

}
