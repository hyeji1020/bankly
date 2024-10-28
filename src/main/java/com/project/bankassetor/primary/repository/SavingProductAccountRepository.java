package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingProductAccountRepository extends JpaRepository<SavingProductAccount, Long> {

    @Query(value = """
        SELECT spa.*
        FROM saving_product_account spa
        JOIN saving_account sa ON spa.savingAccountId = sa.id
        WHERE sa.accountId = :accountId
    """, nativeQuery = true)
    Optional<SavingProductAccount> findByAccountId(@Param("accountId") Long accountId);
}
