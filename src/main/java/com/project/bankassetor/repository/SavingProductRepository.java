package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.account.save.SavingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingProductRepository extends JpaRepository<SavingProduct, Long> {
}
