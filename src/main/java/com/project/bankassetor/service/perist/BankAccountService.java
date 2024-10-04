package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    // accountId로 조회
    public BankAccount findByAccountId(long accountId) {

        // 요청 계좌 번호 확인
        BankAccount bankAccount = bankAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("{}: 에 해당하는 아이디를 찾을 수 없습니다.", accountId);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        return bankAccount;
    }

    // 계좌번호로 조회
    public BankAccount findByAccountNumber(Long accountNumber) {

        // 요청 계좌 번호 확인
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);

        return bankAccount;
    }

}
