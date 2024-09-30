package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(value = "SELECT th.* FROM transaction_history th " +
            "JOIN bank_account ba ON th.bank_account_id = ba.id " +
            "WHERE ba.account_id = :accountId", nativeQuery = true)
    List<TransactionHistory> findHistoriesByAccountId (Long accountId);

}
