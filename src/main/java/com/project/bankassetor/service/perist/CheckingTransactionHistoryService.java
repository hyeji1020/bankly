package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import com.project.bankassetor.primary.model.enums.TransactionType;
import com.project.bankassetor.primary.repository.CheckingTransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // 거래 내역 저장
    public CheckingTransactionHistory save(CheckingAccount checkingAccount, BigDecimal amount, BigDecimal balance, String transactionType) {

        CheckingTransactionHistory toHistory = CheckingTransactionHistory.builder()
                .checkingAccountId(checkingAccount.getId())
                .memberId(checkingAccount.getMemberId())
                .accountId(checkingAccount.getAccountId())
                .time(LocalDateTime.now())
                .amount(amount)
                .balance(balance)
                .type(TransactionType.valueOf(transactionType))
                .build();

        log.info("입출금 거래내역 정보:{}", toJson(toHistory));

        return historyRepository.save(toHistory);
    }

    public List<CheckingTransactionHistory> findCheckTransactionHistoryByAccountId(long accountId) {
        return historyRepository.findByAccountId(accountId);
    }

    public Page<CheckingTransactionHistory> findAllByAccountIdAndType(long accountId, String txType, long memberId, PageRequest pageable) {
        return historyRepository.findAllByAccountIdAndType(accountId, txType, memberId, pageable);
    }
}
