package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.account.save.SavingDuration;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingProductAccountFixture {

    public static SavingProductAccount get(Long accountId, Long savingProductId,
                                           Long savingAccountId, Long userId,
                                           Long savingDurationId) {
        return SavingProductAccount.builder()
                .accountId(accountId)
                .savingProductId(savingProductId)
                .savingAccountId(savingAccountId)
                .userId(userId)
                .savingDurationId(savingDurationId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().minusMonths(3))
                .monthlyDeposit(BigDecimal.valueOf(400000))
                .build();
    }
}
