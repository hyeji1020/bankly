package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    // 계좌 번호로 계좌 찾기
    @Query(value = "SELECT ca.* FROM checking_account ca " +
            "JOIN account a ON ca.accountId = a.id " +
            "WHERE a.accountNumber = :accountNumber", nativeQuery = true)
    CheckingAccount findByAccountNumber(@Param("accountNumber") String accountNumber);

    // 계좌 ID로 계좌 찾기
    @Query(value = "SELECT * FROM checking_account WHERE accountId = :accountId ", nativeQuery = true)
    Optional<CheckingAccount> findByAccountId(@Param("accountId") Long accountId);

}
