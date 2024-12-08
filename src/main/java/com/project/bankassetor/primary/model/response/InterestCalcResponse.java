package com.project.bankassetor.primary.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;


@AllArgsConstructor
@Builder
@Getter
public class InterestCalcResponse {

    private String totalPrincipal;      // 총 납입 원금
    private String totalInterest;       // 예상 만기 이자 (세전)
    private String taxAmount;        // 세후 이자
    private String maturityAmount; // 예상 만기 금액

    private static String formatAmount(BigDecimal amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    public static InterestCalcResponse of(BigDecimal totalPrincipal, BigDecimal totalInterest, BigDecimal taxAmount, BigDecimal maturityAmount) {
        return InterestCalcResponse.builder()
                .totalPrincipal(formatAmount(totalPrincipal))
                .totalInterest(formatAmount(totalInterest))
                .taxAmount(formatAmount(taxAmount))
                .maturityAmount(formatAmount(maturityAmount))
                .build();
    }
}
