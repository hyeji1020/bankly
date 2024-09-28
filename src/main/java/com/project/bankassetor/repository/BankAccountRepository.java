package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BankAccountRepository {
    Map<Long, BankAccount> accountMap = new HashMap<>();

    public Optional<BankAccount> findById(Long accountId) {
        return Optional.ofNullable(accountMap.get(accountId));
    }

    public BankAccount save(BankAccount bankAccount) {
        return accountMap.put(bankAccount.getAccount().getAccountNumber(), bankAccount);
    }

}
