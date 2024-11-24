package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.repository.SavingProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SavingProductServiceTest {

    @Autowired
    private SavingProductService savingProductService;

    @Autowired
    private SavingProductRepository savingProductRepository;

    @BeforeEach
    public void setUp() {
        savingProductRepository.deleteAll();
    }

    @Test
    @DisplayName("10개의 적금 상품을 생성하고 저장한다")
    public void saveSavingProduct() {
        List<SavingProduct> savingProducts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            savingProducts.add(SavingProduct.builder()
                    .name("적금 상품 " + i)
                    .savingLimit(BigDecimal.valueOf(100000 + i * 10000)) // 기본 100,000 + i * 10,000
                    .durationInMonths(6 + i * 2) // 6개월부터 2개월씩 증가
                    .interestRate(BigDecimal.valueOf(3.0 + i * 0.2)) // 3.0%부터 0.2%씩 증가
                    .description("상품 설명" + i)
                    .build());
        }
        savingProductService.saveAll(savingProducts);

        long count = savingProductService.count(); // 데이터 개수 확인 메서드
        Assertions.assertEquals(10, count, "10개의 적금 상품 저장되지 않았습니다.");
    }
}
