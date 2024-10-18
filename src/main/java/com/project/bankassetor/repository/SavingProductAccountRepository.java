package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.account.save.SavingProductAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingProductAccountRepository extends JpaRepository<SavingProductAccount, Long> {
}
