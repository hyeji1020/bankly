package com.project.bankassetor.service.perist;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InterestCalculationService {

    // 총 원금 계산(개월 수 * 금액)
    public BigDecimal calculateTotalPrincipal(BigDecimal monthlyAmount, int duration) {
        return monthlyAmount.multiply(BigDecimal.valueOf(duration));
    }

    // 총 이자 계산
    public BigDecimal calculateTotalInterest(BigDecimal totalPrincipal, BigDecimal interestRate) {
        // 퍼센트(%)로 주어진 이자율을 소수로 변환하여 계산 (예: 4.50% -> 0.045)
        BigDecimal rate = interestRate.divide(BigDecimal.valueOf(100));
        return totalPrincipal.multiply(rate);
    }

    // 이자 소득세 계산 메서드
    public static BigDecimal calculateTaxAmount(BigDecimal totalInterest) {
        BigDecimal taxRate = new BigDecimal("0.154");
        return totalInterest.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    // 만기 금액 계산
    public BigDecimal calculateMaturityAmount(BigDecimal totalPrincipal, BigDecimal totalInterest) {
        BigDecimal taxAmount = calculateTaxAmount(totalInterest);
        return totalPrincipal.add(totalInterest).subtract(taxAmount);
    }

}
