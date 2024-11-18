package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.enums.AccountType;

import java.math.BigDecimal;

public class AccountFixture {

    public static Account get() {
        return Account.builder()
                .accountNumber("1000001")
                .accountStatus(AccountStatus.active)
                .depositLimit(BigDecimal.valueOf(400000))
                .balance(BigDecimal.valueOf(90000))
                .accountType(AccountType.saving)
                .build();
    }
}
