package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.repository.SavingAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingAccountService {

    private final SavingAccountRepository accountRepository;

    // AccountId로 저장하기
    public SavingAccount save(Long accountId) {

        SavingAccount savingAccount = SavingAccount.builder()
                .accountId(accountId)
                .build();

        return accountRepository.save(savingAccount);
    }
}
