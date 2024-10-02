package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // 잔액 변경(입금)
    @Modifying
    @Query(value = "UPDATE account SET balance = balance + :amount WHERE id = :id", nativeQuery = true)
    void depositUpdateBalance(@Param("id") long id, @Param("amount") int amount);

    // 잔액 변경(출금)
    @Modifying
    @Query(value = "UPDATE account SET balance = balance - :amount WHERE id = :id", nativeQuery = true)
    void withdrawUpdateBalance(@Param("id") long id, @Param("amount") int amount);
}
