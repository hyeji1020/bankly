package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.account.check.CheckingTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingTransactionHistoryRepository extends JpaRepository<CheckingTransactionHistory, Long> {

    @Query(value = "SELECT th.* FROM checking_transaction_history th " +
            "JOIN bank_account ba ON th.bank_account_id = ba.id " +
            "WHERE ba.account_id = :accountId", nativeQuery = true)
    List<CheckingTransactionHistory> findHistoriesByAccountId (Long accountId);
  
}
