package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingTransactionHistoryRepository extends JpaRepository<CheckingTransactionHistory, Long> {

    @Query(value = "SELECT th.* FROM checking_tx_history th " +
            "JOIN checking_account ca ON th.bankAccountId = ca.id " +
            "WHERE ca.accountId = :accountId", nativeQuery = true)
    List<CheckingTransactionHistory> findHistoriesByAccountId (@Param("accountId") Long accountId);

    @Query(value = "SELECT cth.* FROM checking_tx_history cth WHERE cth.accountId = :accountId ORDER BY cth.txTime DESC", nativeQuery = true)
    List<CheckingTransactionHistory> findByAccountId(@Param("accountId") long accountId);
}
