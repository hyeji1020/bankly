package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.account.check.BankAccount;
import com.project.bankassetor.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.repository.CheckingTransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckingTransactionHistoryService {

    private final CheckingTransactionHistoryRepository historyRepository;
    private final BankAccountService bankAccountService;

    // 거래 내역 저장
    public CheckingTransactionHistory save(BankAccount bankAccount, int amount, int balance) {

        CheckingTransactionHistory toHistory = CheckingTransactionHistory.builder()
                .bankAccountId(bankAccount.getId())
                .transactionTime(LocalDateTime.now())
                .transactionAmount(amount)
                .balance(balance)
                .build();

        log.info("거래내역 정보:{}", toHistory.toString());

        return historyRepository.save(toHistory);
    }

    // 거래 내역 확인
    public List<CheckingTransactionHistory> findBalanceHistory(Long accountId) {

        bankAccountService.findByAccountId(accountId);

        List<CheckingTransactionHistory> findHistory = historyRepository.findHistoriesByAccountId(accountId);

        return findHistory;
    }
}
