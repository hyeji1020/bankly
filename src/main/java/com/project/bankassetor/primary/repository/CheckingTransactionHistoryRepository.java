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
    List<CheckingTransactionHistory> findByAccountId(@Param("accountId") Long accountId);

    @Query(value = """
        SELECT *
        FROM checking_tx_history
        WHERE id is not null
          AND (:accountId is null or accountId = :accountId)
          AND (:memberId is null or memberId = :memberId)
          AND (:txType is null or (:txType = 'all' OR type = :txType))
        ORDER BY time DESC

    """, countQuery = """
        SELECT count(*)
        FROM checking_tx_history
        WHERE id is not null
          AND (:accountId is null or accountId = :accountId)
          AND (:memberId is null or memberId = :memberId)
          AND (:txType is null or (:txType = 'all' OR type = :txType))
        ORDER BY time DESC;
    """, nativeQuery = true)
    Page<CheckingTransactionHistory> findAllByAccountIdAndType(@Param("accountId") Long accountId,
                                                               @Param("txType") String txType,
                                                               @Param("memberId") Long memberId, PageRequest pageable);

}
