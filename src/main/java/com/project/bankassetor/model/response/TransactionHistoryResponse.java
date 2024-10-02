package com.project.bankassetor.model.response;

import com.project.bankassetor.model.entity.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class TransactionHistoryResponse {

    LocalDateTime transactionTime;  // 거래 시간
    int transactionAmount;  // 거래 금액
    int balanceAfter;  // 거래 후 잔액

    public static List<TransactionHistoryResponse> of(List<TransactionHistory> transactionHistories) {
        return transactionHistories.stream()
                .map(transactionHistory -> TransactionHistoryResponse.builder()
                        .transactionTime(transactionHistory.getTransactionTime())
                        .transactionAmount(transactionHistory.getTransactionAmount())
                        .balanceAfter(transactionHistory.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

}
