package com.project.bankassetor.model.response;

import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.entity.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class BalanceHistoryResponse {

    LocalDateTime transactionTime;  // 거래 시간
    int transactionAmount;  // 거래 금액
    int balanceBefore;  // 거래 전 잔액
    int balanceAfter;  // 거래 후 잔액

    public static BalanceHistoryResponse of(TransactionHistory transactionHistory) {
        return BalanceHistoryResponse.builder()
                .transactionTime(transactionHistory.getTransactionTime())
                .transactionAmount(transactionHistory.getTransactionAmount())
                .balanceBefore(transactionHistory.getBalanceBefore())
                .balanceAfter(transactionHistory.getBalanceAfter())
                .build();
    }

}
