package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import com.project.bankassetor.primary.model.enums.TransactionType;
import com.project.bankassetor.primary.repository.SavingTransactionHistoryRepository;
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
public class SavingTransactionHistoryService {

    private final SavingTransactionHistoryRepository historyRepository;

    public SavingTransactionHistory save(SavingAccount savingAccount, BigDecimal amount, Account account, String transactionType) {

        SavingTransactionHistory toHistory = SavingTransactionHistory.builder()
                .savingAccountId(savingAccount.getId())
                .savingProductId(savingAccount.getSavingProductId())
                .memberId(savingAccount.getMemberId())
                .accountId(account.getId())
                .txTime(LocalDateTime.now())
                .txAmount(amount)
                .balance(account.getBalance())
                .txType(TransactionType.valueOf(transactionType))
                .build();

        log.info("거래내역 정보:{}", toJson(toHistory));

        return historyRepository.save(toHistory);
    }

    public List<SavingTransactionHistory> findSaveTransactionHistoryByAccountId(long accountId) {
        return historyRepository.findByAccountId(accountId);
    }
}
