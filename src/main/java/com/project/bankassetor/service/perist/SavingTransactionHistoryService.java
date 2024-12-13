package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import com.project.bankassetor.primary.model.enums.TransactionType;
import com.project.bankassetor.primary.repository.SavingTransactionHistoryRepository;
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
public class SavingTransactionHistoryService {

    private final SavingTransactionHistoryRepository historyRepository;

    public SavingTransactionHistory save(SavingAccount savingAccount, BigDecimal amount, Account account, String transactionType) {

        SavingTransactionHistory toHistory = SavingTransactionHistory.builder()
                .savingAccountId(savingAccount.getId())
                .savingProductId(savingAccount.getSavingProductId())
                .memberId(savingAccount.getMemberId())
                .accountId(account.getId())
                .time(LocalDateTime.now())
                .amount(amount)
                .balance(account.getBalance())
                .type(TransactionType.valueOf(transactionType))
                .build();

        log.info("적금 계좌 거래내역 정보:{}", toJson(toHistory));

        return historyRepository.save(toHistory);
    }

    public List<SavingTransactionHistory> findSaveTransactionHistoryByAccountId(long accountId) {
        return historyRepository.findByAccountId(accountId);
    }

    public void save(SavingTransactionHistory history) {
        historyRepository.save(history);
    }

    public SavingTransactionHistory savePenalty(long accountId, SavingAccount savingAccount, BigDecimal finalPayment) {
        // 거래 내역 생성 및 저장
        SavingTransactionHistory history = SavingTransactionHistory.builder()
                .accountId(accountId)
                .savingAccountId(savingAccount.getId())
                .memberId(savingAccount.getMemberId())
                .savingProductId(savingAccount.getSavingProductId())
                .balance(finalPayment)
                .type(TransactionType.termination)
                .amount(finalPayment)
                .time(LocalDateTime.now())
                .build();

        log.info("적금 계좌 거래내역 정보:{}", toJson(history));

        return historyRepository.save(history);
    }

    public Page<SavingTransactionHistory> findAllByAccountIdAndType(long accountId, String txType, long memberId, PageRequest pageable) {
        return historyRepository.findAllByAccountIdAndType(accountId, txType, memberId, pageable);
    }
}
