package com.project.bankassetor.service.perist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.project.bankassetor.service.perist.InterestCalculationService.afterTaxInterest;

@SpringBootTest
class InterestCalculationServiceTest {

    @Autowired
    private InterestCalculationService interestCalculationService;

    @Test
    @DisplayName("총 원금을 계산한다.")
    void calculateTotalPrincipal() {

        BigDecimal monthlyAmount = new BigDecimal("700000");
        int duration = 12;

        BigDecimal result = interestCalculationService.totalPrincipal(monthlyAmount, duration);

        Assertions.assertEquals(result, BigDecimal.valueOf(8400000));
    }

    @Test
    @DisplayName("적금 만기 시 세전 이자 계산한다.")
    void calculateBeforeTaxInterest() {

        BigDecimal monthlyDeposit = BigDecimal.valueOf(300000);
        BigDecimal interestRate = new BigDecimal("3.20");
        int depositCount = 12;

        BigDecimal result = interestCalculationService.beforeTaxInterest(monthlyDeposit, interestRate, depositCount)
                .setScale(0, RoundingMode.HALF_UP);

        Assertions.assertEquals(result, BigDecimal.valueOf(62400));
    }

    @Test
    @DisplayName("적금 만기 시 세후 이자 계산한다.")
    void calculateAfterTaxInterest() {

        BigDecimal beforeInterest = BigDecimal.valueOf(62400);
        BigDecimal result = afterTaxInterest(beforeInterest).setScale(0, RoundingMode.HALF_UP);

        Assertions.assertEquals(result, BigDecimal.valueOf(52790));
    }


    @Test
    @DisplayName("적금 만기 시 총 지급액을 계산한다.")
    void calculateMaturityAmount() {

        BigDecimal monthlyDeposit = BigDecimal.valueOf(300000);
        int depositCount = 12;
        BigDecimal interestRate = new BigDecimal("3.20");

        BigDecimal result = interestCalculationService.maturityAmount(monthlyDeposit, depositCount, interestRate);

        Assertions.assertEquals(result, BigDecimal.valueOf(3652790));
    }

    @Test
    @DisplayName("중도해지 시 세전 이자를 계산한다.")
    void calculateTermInterest() {

        LocalDate startDate = LocalDate.of(2024, 7, 28);
        BigDecimal principal  = BigDecimal.valueOf(500000);
        BigDecimal termInterestRate  = new BigDecimal("0.5");

        BigDecimal result = interestCalculationService.termInterest(startDate, principal, termInterestRate);

        Assertions.assertEquals(result.setScale(0, RoundingMode.HALF_UP), BigDecimal.valueOf(836));
    }

    @Test
    @DisplayName("중도해지시 최종 지급액을 계산한다.")
    void penaltyAmount() {

        LocalDate startDate = LocalDate.of(2024, 7, 28);
        int depositCount = 5;
        BigDecimal monthlyDeposit = BigDecimal.valueOf(100000);
        BigDecimal annualRate  = new BigDecimal("0.5");

        BigDecimal result = interestCalculationService.terminateAmount(monthlyDeposit, depositCount, annualRate , startDate);

        Assertions.assertEquals(result, BigDecimal.valueOf(500707));

    }
}