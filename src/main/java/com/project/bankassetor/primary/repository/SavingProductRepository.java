package com.project.bankassetor.primary.repository;

import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingProductRepository extends JpaRepository<SavingProduct, Long> {

    @Query(value = """
        SELECT *
        FROM saving_product
        WHERE (:keyword IS NULL OR name LIKE CONCAT('%', :keyword, '%'))
    """, countQuery = """
        SELECT count(*)
        FROM saving_product
        WHERE (:keyword IS NULL OR name LIKE CONCAT('%', :keyword, '%'))
    """, nativeQuery = true)
    Page<SavingProduct> getAllSavingProducts(@Param("keyword") String keyword, PageRequest pageable);
}
