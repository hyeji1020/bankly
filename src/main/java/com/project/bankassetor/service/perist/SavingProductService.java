package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.request.InterestCalcRequest;
import com.project.bankassetor.primary.model.response.InterestCalcResponse;
import com.project.bankassetor.primary.repository.SavingProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.project.bankassetor.service.perist.InterestCalculationService.afterTaxInterest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingProductService {

    private final SavingProductRepository savingProductRepository;
    private final InterestCalculationService calculationService;


    public List<SavingProduct> findAll(){
        return savingProductRepository.findAll();
    }

    // 적금 상품 아이디로 조회
    public SavingProduct findById(Long id){
        return savingProductRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{}: 아이디에 해당하는 적금 상품을 찾을 수 없습니다.", id);
                    return new AccountNotFoundException(ErrorCode.SAVING_ACCOUNT_NOT_FOUND);
                });
    }

    // 적금 상품 이자 계산기 메서드
    public InterestCalcResponse calculateInterest(long savingProductId, InterestCalcRequest request) {
        SavingProduct savingProduct = findById(savingProductId);

        BigDecimal totalPrincipal = calculationService.totalPrincipal(request.getMonthlyDeposit(), savingProduct.getDurationInMonths());
        BigDecimal beforeTaxInterest = calculationService.beforeTaxInterest(request.getMonthlyDeposit(), savingProduct.getInterestRate(), savingProduct.getDurationInMonths());
        BigDecimal afterTaxInterest = afterTaxInterest(beforeTaxInterest);
        BigDecimal maturityAmount = calculationService.maturityAmount(request.getMonthlyDeposit(), savingProduct.getDurationInMonths(), savingProduct.getInterestRate());

        return InterestCalcResponse.of(totalPrincipal, beforeTaxInterest, afterTaxInterest, maturityAmount);
    }

    public void saveAll(List<SavingProduct> savingProducts) {
        savingProductRepository.saveAll(savingProducts);
    }

    public long count() {
        return savingProductRepository.count();
    }

    public Page<SavingProduct> getAllSavingProducts(String keyword, PageRequest pageable) {
        return savingProductRepository.getAllSavingProducts(keyword, pageable);
    }
}
