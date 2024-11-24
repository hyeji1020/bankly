package com.project.bankassetor.service.perist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class InterestCalculationServiceTest {

    @Autowired
    private InterestCalculationService interestCalculationService;

    @Test
    @DisplayName("총 원금을 계산한다.")
    void calculateTotalPrincipal() {
        BigDecimal monthlyAmount = new BigDecimal("700000");
        int duration = 12;

        BigDecimal result = interestCalculationService.calculateTotalPrincipal(monthlyAmount, duration);

        Assertions.assertEquals(result, BigDecimal.valueOf(8400000));
    }
//    @Test
//    @DisplayName("이자(세전)를 계산한다.")
//    void calculateTotalInterest() {
//        SavingProduct savingProduct = SavingProductFixture.get(SavingDurationFixture.get());
//        BigDecimal monthlyAmount = new BigDecimal("700000");
//
//        BigDecimal result = interestCalculationService.calculateTotalInterest(monthlyAmount, savingProduct);
//
//        Assertions.assertEquals(result, BigDecimal.valueOf(159250));
//    }

    @Test
    @DisplayName("이자소득세를 계산한다.")
    void calculateTaxAmount() {
        BigDecimal totalInterest = new BigDecimal(159250);
        BigDecimal result = interestCalculationService.calculateTaxAmount(totalInterest);

        Assertions.assertEquals(result, BigDecimal.valueOf(24524));
    }

    @Test
    @DisplayName("이자(세후)를 계산한다.")
    void calculateInterest() {
        BigDecimal totalInterest = new BigDecimal(159250);
        BigDecimal taxAmount = interestCalculationService.calculateTaxAmount(totalInterest);

        BigDecimal result = totalInterest.subtract(taxAmount);

        Assertions.assertEquals(result, BigDecimal.valueOf(134726	));
    }

    @Test
    @DisplayName("만기지급액을 계산한다.")
    void calculateMaturityAmount() {
        BigDecimal totalPrincipal = new BigDecimal("8400000");
        BigDecimal totalInterest = new BigDecimal("159250");

        BigDecimal result = interestCalculationService.calculateMaturityAmount(totalPrincipal, totalInterest);

        Assertions.assertEquals(result, BigDecimal.valueOf(8534726));
    }
}