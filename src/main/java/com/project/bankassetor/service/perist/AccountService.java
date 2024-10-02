package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // 계좌 잔액 변경(입금)
    @Transactional
    public void depositUpdateBalance(long id, int amount) {
        accountRepository.depositUpdateBalance(id, amount);
    }

    // 계좌 잔액 변경(출금)
    @Transactional
    public void withdrawUpdateBalance(long id, int amount) {
        accountRepository.withdrawUpdateBalance(id, amount);
    }
}
