package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    @Query(value = """
    SELECT *
    FROM checking_account
    WHERE account_id = (
        SELECT id
        FROM account
        WHERE account_number = :accountNumber
    )
    """, nativeQuery = true)
    Optional<CheckingAccount> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
