package com.project.bankassetor.service.perist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class InterestCalculationService {

    // 총 원금 계산(개월 수 * 금액)
    public BigDecimal totalPrincipal(BigDecimal monthlyAmount, int duration) {
        return monthlyAmount.multiply(BigDecimal.valueOf(duration));
    }

    // 총 이자 계산
    public BigDecimal totalInterest(BigDecimal totalPrincipal, BigDecimal interestRate, int depositCount) {
        // 퍼센트(%)로 주어진 이자율을 소수로 변환하여 계산 (예: 4.50% -> 0.045)
         return totalPrincipal.multiply(interestRate.divide(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(depositCount).divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)));

    }

    // 이자 소득세 계산 메서드
    public static BigDecimal taxAmount(BigDecimal totalInterest) {
        BigDecimal taxRate = new BigDecimal("0.154");
        return totalInterest.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    // 만기 금액 계산
    public BigDecimal maturityAmount(BigDecimal totalPrincipal, BigDecimal totalInterest) {
        BigDecimal taxAmount = taxAmount(totalInterest);
        return totalPrincipal.add(totalInterest).subtract(taxAmount);
    }

    // 패널티 계산
    public BigDecimal terminateAmount(BigDecimal monthlyDeposit, int depositCount, BigDecimal interestRate, BigDecimal penaltyRate) {

        // 원금
        BigDecimal totalPrincipal = monthlyDeposit.multiply(BigDecimal.valueOf(depositCount));

        // 이자
        BigDecimal interestRateDecimal = interestRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal totalInterest = totalPrincipal.multiply(interestRateDecimal)
                .multiply(BigDecimal.valueOf(depositCount).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP));

        // 패널티(원금 * 패널티%)
        BigDecimal penaltyAmount = totalPrincipal.multiply(penaltyRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        // (원금+이자) - 패널티
        BigDecimal terminateAmount = totalPrincipal.add(totalInterest).subtract(penaltyAmount);

        log.info("원금: {}, 이자: {}, 패널티: {}, 지급액: {}", totalPrincipal, totalInterest, penaltyAmount, terminateAmount);

        return terminateAmount;

    }

    // 적금 만기 금액 계산
    public BigDecimal maturityAmount(BigDecimal monthlyDeposit, int depositCount, BigDecimal interestRate) {

        // 원금
        BigDecimal totalPrincipal = monthlyDeposit.multiply(BigDecimal.valueOf(depositCount));

        // 이자
        BigDecimal interestRateDecimal = interestRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal totalInterest = totalPrincipal.multiply(interestRateDecimal)
                .multiply(BigDecimal.valueOf(depositCount).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP));

        // 이자 소득세 (이자 * 15.4%)
        BigDecimal taxRate = new BigDecimal("0.154");
        BigDecimal taxAmount =  totalInterest.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        // (원금+이자) - 이자 소득세
        BigDecimal maturityAmount = totalPrincipal.add(totalInterest).subtract(taxAmount);

        log.info("원금: {}, 이자: {}, 이자 소득세: {} 만기 금액: {}",
                totalPrincipal, totalInterest, taxAmount, maturityAmount);

        return maturityAmount;
    }

}
