package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.repository.CheckingAccountRepository;
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

    public CheckingAccount findByAccountNumber(String accountNumber) {

        return checkingAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("계좌번호 {}: 에 해당하는 계좌를 찾을 수 없습니다.",accountNumber);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }
}
