package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;

public class SavingAccountFixture {
    public static SavingAccount get(Long accountId) {
        return SavingAccount.builder()
                .accountId(accountId)
                .build();
    }
}
