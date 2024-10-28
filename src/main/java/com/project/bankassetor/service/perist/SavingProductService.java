package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.repository.SavingProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingProductService {

    private final SavingProductRepository savingProductRepository;

    // 적금 상품 아이디로 조회
    public SavingProduct findById(Long id){
        return savingProductRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{}: 아이디에 해당하는 적금 상품을 찾을 수 없습니다.", id);
                    throw new AccountNotFoundException(ErrorCode.SAVING_ACCOUNT_NOT_FOUND);
                });
    }

}
