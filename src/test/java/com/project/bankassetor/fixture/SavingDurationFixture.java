package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.account.save.SavingDuration;

public class SavingDurationFixture {

    public static SavingDuration get() {
        return SavingDuration.builder()
                .description("3개월")
                .durationInMonths(3)
                .build();
    }
}
