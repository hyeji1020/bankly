package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // 계좌 번호로 조회
    @Query(value = "SELECT a.* FROM account a WHERE a.account_number = :accountNumber", nativeQuery = true)
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    Optional<Account> findFirstByOrderByIdDesc();

    @Query(value = "SELECT a.* FROM account a JOIN bank_account ba ON a.id = ba.accountId WHERE ba.memberId = :memberId", nativeQuery = true)
    Optional<List<Account>> findCheckByMemberId(@Param("memberId") long memberId);

    @Query(value = "SELECT a.* FROM account a JOIN saving_product_account sa ON a.id = sa.accountId WHERE sa.memberId = :memberId", nativeQuery = true)
    Optional<List<Account>> findSaveByMemberId(@Param("memberId") long memberId);
}
