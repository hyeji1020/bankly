package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;
import com.project.bankassetor.primary.repository.SavingProductAccountRepository;
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

    public SavingProductAccount findByAccountId(Long accountId) {

        return productAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("{}: 아이디에 해당하는 적금 계좌를 찾을 수 없습니다.", accountId);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }
}
