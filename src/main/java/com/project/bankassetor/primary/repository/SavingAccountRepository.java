package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {

    @Query(value = """
        SELECT sa.*
        FROM saving_account sa
        JOIN account a ON sa.accountId = a.id
        WHERE a.id = :accountId
    """, nativeQuery = true)
    Optional<SavingAccount> findByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT sa.* FROM saving_account sa WHERE sa.endDate = :now", nativeQuery = true)
    List<SavingAccount> findAllByEndDateBefore(LocalDate now);
}
