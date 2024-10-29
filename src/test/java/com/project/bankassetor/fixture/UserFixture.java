package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.User;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserFixture {

    public static User get() {
        return User.builder()
                .name("User1")
                .build();
    }
}
