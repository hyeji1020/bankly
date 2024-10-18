package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.repository.CheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckingAccountService {

    private final CheckingAccountRepository checkingAccountRepository;

    public CheckingAccount save(Account account) {

        CheckingAccount checkingAccount = CheckingAccount.builder()
                .accountId(account.getId())
                .build();

        return checkingAccountRepository.save(checkingAccount);
    }
}
