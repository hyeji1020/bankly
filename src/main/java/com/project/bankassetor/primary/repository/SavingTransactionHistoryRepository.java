package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionHistoryRepository extends JpaRepository<SavingTransactionHistory, Long> {

    @Query(value = "SELECT * FROM saving_transaction_history sth WHERE sth.memberId = :memberId AND sth.accountId = :accountId", nativeQuery = true)
    List<SavingTransactionHistory> findByMemberId(@Param("memberId") long memberId, @Param("accountId") long accountId);
}
