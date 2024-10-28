package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
}
