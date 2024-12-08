package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingTransactionHistoryRepository extends JpaRepository<CheckingTransactionHistory, Long> {

    @Query(value = "SELECT cth.* FROM checking_tx_history cth WHERE cth.accountId = :accountId ORDER BY cth.time DESC", nativeQuery = true)
    List<CheckingTransactionHistory> findByAccountId(@Param("accountId") long accountId);

    @Query(value = """
        SELECT *
        FROM checking_tx_history
        WHERE accountId = :accountId  AND memberId = :memberId AND (:txType = 'all' OR type = :txType)

    """, countQuery = """
        SELECT count(*)
        FROM checking_tx_history
        WHERE accountId = :accountId  AND memberId = :memberId AND (:txType = 'all' OR type = :txType)
    """, nativeQuery = true)
    Page<CheckingTransactionHistory> findAllByAccountIdAndType(@Param("accountId") long accountId, @Param("txType") String txType, @Param("memberId") long memberId, PageRequest pageable);
}
