package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.entity.TransactionHistory;
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
    private final BankAccountService bankAccountService;

    // 거래 내역 저장
    public TransactionHistory save(BankAccount bankAccount, int amount, int balance) {

        TransactionHistory toHistory = TransactionHistory.builder()
                .bankAccount(bankAccount)
                .transactionTime(LocalDateTime.now())
                .transactionAmount(amount)
                .balance(balance)
                .build();

        log.info("거래내역 정보:{}", toHistory.toString());

        return historyRepository.save(toHistory);
    }

    // 거래 내역 확인
    public List<TransactionHistory> findBalanceHistory(Long accountId) {

        bankAccountService.findByAccountId(accountId);

        List<TransactionHistory> findHistory = historyRepository.findHistoriesByAccountId(accountId);

        return findHistory;
    }
}
