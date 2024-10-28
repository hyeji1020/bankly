package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    // 계좌 번호로 계좌 찾기
    @Query(value = "SELECT ba.* FROM bank_account ba " +
            "JOIN account a ON ba.account_id = a.id " +
            "WHERE a.account_number = :accountNumber", nativeQuery = true)
   BankAccount findByAccountNumber(@Param("accountNumber") String accountNumber);

    // 계좌 ID로 계좌 찾기
    @Query(value = "SELECT * FROM bank_account WHERE accountId = :accountId ", nativeQuery = true)
    Optional<BankAccount> findByAccountId(@Param("accountId") Long accountId);

}
