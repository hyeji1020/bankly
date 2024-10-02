package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.entity.TransactionHistory;
import com.project.bankassetor.repository.BankAccountRepository;
import com.project.bankassetor.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionHistoryRepository historyRepository;
    private final BankAccountRepository bankAccountRepository;

    // 거래 내역 저장
    public TransactionHistory save(BankAccount bankAccount, int amount, int balance) {

        TransactionHistory toHistory = TransactionHistory.builder()
                .bankAccount(bankAccount)
                .transactionTime(LocalDateTime.now())
                .transactionAmount(amount)
                .balanceBefore(bankAccount.getAccount().getBalance())
                .balanceAfter(balance)
                .build();

        log.info("거래내역 정보:{}", toHistory.toString());

        return historyRepository.save(toHistory);
    }

    // 거래 내역 확인
    public List<TransactionHistory> findBalanceHistory(Long accountId) {

        BankAccount bankAccount = bankAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("계좌번호 아이디: {}에 해당하는 계좌를 찾을 수 없습니다.", accountId);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        List<TransactionHistory> findHistory = historyRepository.findHistoriesByAccountId(accountId);

        return findHistory;
    }
}
