package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // 계좌 번호로 조회
    @Query(value = "SELECT a.* FROM account a WHERE a.accountNumber = :accountNumber", nativeQuery = true)
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    Optional<Account> findFirstByOrderByIdDesc();

    @Query(value = "SELECT a.* FROM account a JOIN checking_account ca ON a.id = ca.accountId WHERE ca.memberId = :memberId", nativeQuery = true)
    Optional<List<Account>> findCheckByMemberId(@Param("memberId") long memberId);

    @Query(value = "SELECT a.* FROM account a JOIN saving_account sa ON a.id = sa.accountId WHERE sa.memberId = :memberId", nativeQuery = true)
    Optional<List<Account>> findSaveByMemberId(@Param("memberId") long memberId);

    @Query(value = """
        SELECT *
        FROM account
        WHERE (id IN (SELECT accountId FROM checking_account WHERE memberId = :memberId) AND (:accountType = 'all' OR type = :accountType)
            OR id IN (SELECT accountId FROM saving_account WHERE memberId = :memberId) AND (:accountType = 'all' OR type = :accountType))
        AND (:keyword IS NULL OR accountNumber LIKE CONCAT('%', :keyword, '%'))

    """, countQuery = """
        SELECT count(*)
        FROM account
        WHERE (id IN (SELECT accountId FROM checking_account WHERE memberId = :memberId) AND (:accountType = 'all' OR type = :accountType)
            OR id IN (SELECT accountId FROM saving_account WHERE memberId = :memberId)  AND (:accountType = 'all' OR type = :accountType))
          AND (:keyword IS NULL OR accountNumber LIKE CONCAT('%', :keyword, '%'))
    """, nativeQuery = true)
    Page<Account> findAllByMemberId(@Param("accountType") String accountType, @Param("keyword") String keyword, long memberId, Pageable pageable);
}
