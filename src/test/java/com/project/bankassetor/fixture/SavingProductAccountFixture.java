package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingProductAccountFixture {

    public static SavingProductAccount get(Long accountId, Long savingProductId,
                                           Long memberId,
                                           Long savingDurationId) {
        return SavingProductAccount.builder()
                .accountId(accountId)
                .savingProductId(savingProductId)
                .memberId(memberId)
                .savingDurationId(savingDurationId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().minusMonths(3))
                .monthlyDeposit(BigDecimal.valueOf(400000))
                .build();
    }

    public static SavingProductAccount get() {
        return SavingProductAccount.builder()
                .accountId(1L)
                .savingProductId(1L)
                .memberId(1L)
                .savingDurationId(1L)
                .startDate(LocalDate.now().minusMonths(SavingDurationFixture.get().getDurationInMonths()))
                .endDate(LocalDate.now())
                .monthlyDeposit(BigDecimal.valueOf(400000))
                .build();
    }
}
