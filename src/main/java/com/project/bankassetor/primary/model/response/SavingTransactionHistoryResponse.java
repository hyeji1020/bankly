package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class SavingTransactionHistoryResponse {

    private long accountId;
    private long savingProductId;
    private String transactionAmount;  // 거래 금액
    private String balanceAfter;  // 거래 후 잔액
    private String type;
    private String transactionTime;  // 거래 시간

    private static String formatAmount(BigDecimal amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    private static String formatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static List<SavingTransactionHistoryResponse> of(List<SavingTransactionHistory> transactionHistories) {
        return transactionHistories.stream()
                .map(savingTransactionHistory -> SavingTransactionHistoryResponse.builder()
                        .accountId(savingTransactionHistory.getAccountId())
                        .savingProductId(savingTransactionHistory.getSavingProductId())
                        .transactionTime(formatter(savingTransactionHistory.getTime()))
                        .transactionAmount(formatAmount(savingTransactionHistory.getAmount()))
                        .balanceAfter(formatAmount(savingTransactionHistory.getBalance()))
                        .type(savingTransactionHistory.getType().getDisplayName())
                        .build())
                .collect(Collectors.toList());
    }

    public static SavingTransactionHistoryResponse of(SavingTransactionHistory savingTransactionHistory) {
        return SavingTransactionHistoryResponse.builder()
                        .accountId(savingTransactionHistory.getAccountId())
                        .savingProductId(savingTransactionHistory.getSavingProductId())
                        .transactionTime(formatter(savingTransactionHistory.getTime()))
                        .transactionAmount(formatAmount(savingTransactionHistory.getAmount()))
                        .balanceAfter(formatAmount(savingTransactionHistory.getBalance()))
                        .type(savingTransactionHistory.getType().getDisplayName())
                        .build();
        }

}
