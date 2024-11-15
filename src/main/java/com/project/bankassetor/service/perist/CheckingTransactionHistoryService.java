package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.primary.model.enums.TransactionType;
import com.project.bankassetor.primary.repository.CheckingTransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckingTransactionHistoryService {

    private final CheckingTransactionHistoryRepository historyRepository;
    private final BankAccountService bankAccountService;
    private final CheckingTransactionHistoryRepository checkingTransactionHistoryRepository;

    // 거래 내역 저장
    public CheckingTransactionHistory save(BankAccount bankAccount, BigDecimal amount, BigDecimal balance, String transactionType) {

        CheckingTransactionHistory toHistory = CheckingTransactionHistory.builder()
                .bankAccountId(bankAccount.getId())
                .memberId(bankAccount.getMemberId())
                .checkingAccountId(bankAccount.getCheckingAccountId())
                .accountId(bankAccount.getAccountId())
                .transactionTime(LocalDateTime.now())
                .transactionAmount(amount)
                .balance(balance)
                .transactionType(TransactionType.valueOf(transactionType))
                .build();

        log.info("거래내역 정보:{}", toJson(toHistory));

        return historyRepository.save(toHistory);
    }

    // 거래 내역 확인
    public List<CheckingTransactionHistory> findBalanceHistory(Long accountId) {

        List<CheckingTransactionHistory> findHistory = historyRepository.findHistoriesByAccountId(accountId);

        return findHistory;
    }

    public List<CheckingTransactionHistory> findCheckTransactionHistoryByAccountId(long accountId) {
        return checkingTransactionHistoryRepository.findByAccountId(accountId);
    }
}
