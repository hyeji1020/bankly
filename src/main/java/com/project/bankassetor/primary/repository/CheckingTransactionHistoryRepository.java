package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingTransactionHistoryRepository extends JpaRepository<CheckingTransactionHistory, Long> {

    @Query(value = "SELECT th.* FROM checking_transaction_history th " +
            "JOIN bank_account ba ON th.bankAccountId = ba.id " +
            "WHERE ba.accountId = :accountId", nativeQuery = true)
    List<CheckingTransactionHistory> findHistoriesByAccountId (Long accountId);

    @Query(value = "SELECT * FROM checking_transaction_history cth WHERE cth.id = :memberId AND cth.accountId = :accountId", nativeQuery = true)
    List<CheckingTransactionHistory> findByMemberId(@Param("memberId") long memberId, @Param("accountId") long accountId);
}
