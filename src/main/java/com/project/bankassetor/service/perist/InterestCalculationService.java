package com.project.bankassetor.service.perist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class InterestCalculationService {

    // 총 원금 계산(개월 수 * 금액)
    public BigDecimal totalPrincipal(BigDecimal monthlyDeposit, int depositCount) {
        BigDecimal principal = monthlyDeposit.multiply(BigDecimal.valueOf(depositCount));
        log.info("총 원금: {}",principal);
        return principal;
    }

    // 만기 세전 이자 계산
    public BigDecimal beforeTaxInterest(BigDecimal monthlyDeposit, BigDecimal interestRate, int depositCount) {

        // 1. 연이율을 소수로 변환
        BigDecimal annualRateDecimal = interestRate.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP);

        // 2. 개월 수 계산: (개월 수 + 1) × 개월 수 ÷ 2
        BigDecimal monthsFactor = BigDecimal.valueOf(depositCount)
                .add(BigDecimal.ONE) // (개월 수 + 1)
                .multiply(BigDecimal.valueOf(depositCount)) // × 개월 수
                .divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP); // ÷ 2

        // 3. 세전 이자 계산
        BigDecimal beforeTaxInterest = monthlyDeposit
                .multiply(annualRateDecimal) // 월 납입금 × 연이율
                .multiply(monthsFactor) // × 개월 수 계산 결과
                .divide(BigDecimal.valueOf(12), 5, RoundingMode.HALF_UP); // ÷ 12
        log.info("개월 수 계산 (평균 납입 기간): {} 일, 이자(세전): {}", monthsFactor, beforeTaxInterest);

        return beforeTaxInterest;
    }

    // 중도해지 세전 이자 계산
    public BigDecimal termInterest(LocalDate startDate, BigDecimal principal, BigDecimal termInterestRate) {

        long daysElapsed = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        BigDecimal termInterest = principal
                .multiply(termInterestRate.divide(BigDecimal.valueOf(100).setScale(5, RoundingMode.HALF_UP)))
                .multiply(BigDecimal.valueOf(daysElapsed).divide(BigDecimal.valueOf(365), 5, RoundingMode.HALF_UP)) // 경과 기간 비율
                .setScale(2, RoundingMode.HALF_UP);
        log.info("적금 시작일로부터 경과 일수: {} 일, 중도해지 이자(세전) : {} 원", daysElapsed, termInterest);

        return termInterest;
    }

    // 이자 소득세 세후 이자 계산
    public static BigDecimal afterTaxInterest(BigDecimal beforeInterest) {
        BigDecimal taxRate = new BigDecimal("0.154");
        BigDecimal afterTaxInterest = beforeInterest
                .multiply(BigDecimal.ONE.subtract(taxRate)).setScale(2, RoundingMode.HALF_UP);
        log.info("이자(세후) : {} 원", afterTaxInterest);
        return afterTaxInterest;
    }

    // 만기 금액 계산
    public BigDecimal totalAmount(BigDecimal principal, BigDecimal afterTaxInterest) {
        BigDecimal totalAmount = principal.add(afterTaxInterest)
                .setScale(0, RoundingMode.HALF_UP);
        log.info("총 지급액 계산 결과: {}", principal);

        return totalAmount;
    }

    // 패널티 계산
    public BigDecimal terminateAmount(BigDecimal monthlyDeposit, int depositCount, BigDecimal termInterestRate, LocalDate startDate) {

        BigDecimal principal = totalPrincipal(monthlyDeposit, depositCount);

        BigDecimal termInterest = termInterest(startDate, principal, termInterestRate);

        BigDecimal afterTaxInterest = afterTaxInterest(termInterest);

        return totalAmount(principal, afterTaxInterest);
    }

    // 적금 만기 금액 계산
    public BigDecimal maturityAmount(BigDecimal monthlyDeposit, int depositCount, BigDecimal interestRate) {

        BigDecimal principal = totalPrincipal(monthlyDeposit, depositCount);

        BigDecimal beforeTaxInterest = beforeTaxInterest(monthlyDeposit, interestRate, depositCount);

        BigDecimal afterTaxInterest = afterTaxInterest(beforeTaxInterest);

        return totalAmount(principal, afterTaxInterest);
    }

}
