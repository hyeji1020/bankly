package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
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
public class SavingTransactionHistoryResponse {

    private LocalDateTime transactionTime;  // 거래 시간
    private BigDecimal transactionAmount;  // 거래 금액
    private BigDecimal balanceAfter;  // 거래 후 잔액
    private String type;

    public static List<SavingTransactionHistoryResponse> of(List<SavingTransactionHistory> transactionHistories) {
        return transactionHistories.stream()
                .map(savingTransactionHistory -> SavingTransactionHistoryResponse.builder()
                        .transactionTime(savingTransactionHistory.getTransactionTime())
                        .transactionAmount(savingTransactionHistory.getTransactionAmount())
                        .balanceAfter(savingTransactionHistory.getBalance())
                        .type(savingTransactionHistory.getTransactionType().getDisplayName())
                        .build())
                .collect(Collectors.toList());
    }
}
