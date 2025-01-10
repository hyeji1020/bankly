package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionHistoryRepository extends JpaRepository<SavingTransactionHistory, Long> {

    @Query(value = "SELECT sth.* FROM saving_tx_history sth WHERE sth.accountId = :accountId ORDER BY sth.time DESC", nativeQuery = true)
    List<SavingTransactionHistory> findByAccountId(@Param("accountId") Long accountId);

    @Query(value = """
        SELECT *
        FROM saving_tx_history
        WHERE id is not null
          AND (:accountId is null or accountId = :accountId)
          AND (:memberId is null or memberId = :memberId) 
          AND (:txType is null or (:txType = 'all' OR type = :txType))
        ORDER BY time DESC;

    """, countQuery = """
        SELECT count(*)
        FROM saving_tx_history
        WHERE id is not null
          AND (:accountId is null or accountId = :accountId)
          AND (:memberId is null or memberId = :memberId)
          AND (:txType is null or (:txType = 'all' OR type = :txType))
        ORDER BY time DESC;
    """, nativeQuery = true)
    Page<SavingTransactionHistory> findAllByAccountIdAndType(@Param("accountId") Long accountId,
                                                             @Param("txType") String txType,
                                                             @Param("memberId") Long memberId,
                                                             PageRequest pageable);
}
