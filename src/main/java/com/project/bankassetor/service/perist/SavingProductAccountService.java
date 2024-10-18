package com.project.bankassetor.service.perist;

import com.project.bankassetor.model.entity.account.save.SavingAccount;
import com.project.bankassetor.model.entity.account.save.SavingProduct;
import com.project.bankassetor.model.entity.account.save.SavingProductAccount;
import com.project.bankassetor.repository.SavingProductAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingProductAccountService {

    private final SavingProductAccountRepository productAccountRepository;

    public SavingProductAccount save(Long userId, Long savingProductId, Long savingAccountId,
                                     SavingProduct savingProduct, BigDecimal monthlyDeposit){

        SavingProductAccount savingProductAccount = SavingProductAccount.builder()
                .savingProductId(savingProductId)
                .savingAccountId(savingAccountId)
                .userId(userId)
                .monthlyDeposit(monthlyDeposit)
                .startDate(LocalDate.now()) // 시작 날짜
                .endDate(LocalDate.now().plusMonths(savingProduct.getSavingDuration().getDurationInMonths()))
                .build();

        return productAccountRepository.save(savingProductAccount);
    }
}
