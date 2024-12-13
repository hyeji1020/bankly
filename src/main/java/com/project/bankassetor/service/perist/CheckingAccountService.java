package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.repository.CheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckingAccountService {

    private final CheckingAccountRepository checkingAccountRepository;

    // accountId로 조회
    public CheckingAccount findByAccountId(long accountId) {

        return checkingAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("입출금 계좌 조회 중 : {}: 에 해당하는 아이디를 찾을 수 없습니다.", accountId);
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }

    // 계좌번호로 조회
    public CheckingAccount findByAccountNumber(String accountNumber) {

        return checkingAccountRepository.findByAccountNumber(accountNumber);
    }

    // 계좌 생성시 BankAccount에도 저장
    public CheckingAccount save(Long memberId, Long accountId) {

        CheckingAccount checkingAccount = CheckingAccount.builder()
                .memberId(memberId)
                .accountId(accountId)
                .build();

        return checkingAccountRepository.save(checkingAccount);
    }

    public void saveAll(List<CheckingAccount> chAccounts) {
        checkingAccountRepository.saveAll(chAccounts);
    }

    public long count() {
        return checkingAccountRepository.count();
    }
}
