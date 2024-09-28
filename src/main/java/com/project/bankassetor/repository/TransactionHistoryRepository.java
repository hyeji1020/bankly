package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.TransactionHistory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TransactionHistoryRepository {

    private Map<Long, TransactionHistory> historyMap = new HashMap<>();

    private long currentId = 0;

    public TransactionHistory save(TransactionHistory transactionHistory) {
        currentId++;
        historyMap.put(currentId, transactionHistory);
        return transactionHistory;
    }

    public List<TransactionHistory> findHistoriesByAccountId (Long accountId) {
        return historyMap.values().stream()
                .filter(transactionHistory
                        -> transactionHistory.getBankAccount().getAccount().getId() == accountId)
                .collect(Collectors.toList());
    }
}
