package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.account.save.SavingDuration;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;

import java.math.BigDecimal;

    public class SavingProductFixture {

        public static SavingProduct get(SavingDuration savingDuration) {
            return SavingProduct.builder()
                    .name("청년 적금")
                    .savingLimit(BigDecimal.valueOf(10000000))
                    .savingDuration(savingDuration)
                    .interestRate(BigDecimal.valueOf(4.5))
                    .build();
        }
}
