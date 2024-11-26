package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.repository.SavingProductRepository;
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
        savingProductRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("10개의 적금 상품을 생성하고 저장한다")
    public void saveSavingProduct() {
        List<SavingProduct> savingProducts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {

            BigDecimal savingLimit = BigDecimal.valueOf((i % 5 + 1) * 100000);

            savingProducts.add(SavingProduct.builder()
                    .name("적금 상품 " + i)
                    .savingLimit(savingLimit)
                    .durationInMonths(4 + i * 2) // 4개월부터 2개월씩 증가(6~24개월)
                    .interestRate(BigDecimal.valueOf(3.0 + i * 0.2)) // 3.0%부터 0.2%씩 증가
                    .penaltyRate(BigDecimal.valueOf(0.1 + i * 0.1))
                    .description("상품 설명" + i)
                    .build());
        }
        savingProductService.saveAll(savingProducts);

        long count = savingProductService.count(); // 데이터 개수 확인 메서드
        Assertions.assertEquals(10, count, "10개의 적금 상품 저장되지 않았습니다.");
    }
}
