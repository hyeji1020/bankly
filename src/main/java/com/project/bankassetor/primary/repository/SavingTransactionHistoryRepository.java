package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionHistoryRepository extends JpaRepository<SavingTransactionHistory, Long> {

    @Query(value = "SELECT sth.* FROM saving_tx_history sth WHERE sth.accountId = :accountId ORDER BY sth.time DESC", nativeQuery = true)
    List<SavingTransactionHistory> findByAccountId(@Param("accountId") long accountId);
}
