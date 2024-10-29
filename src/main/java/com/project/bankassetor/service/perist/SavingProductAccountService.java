package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.repository.AccountRepository;
import com.project.bankassetor.primary.repository.SavingProductAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingProductAccountService {

    private final SavingProductAccountRepository productAccountRepository;
    private final AccountRepository accountRepository;

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

    @Transactional
    public void expireMaturedAccounts() {
        // 현재 날짜 이전에 만기가 도래한 계좌를 조회
        List<SavingProductAccount> accounts = productAccountRepository.findAllByEndDateBefore(LocalDate.now()).
                orElseThrow(() -> new BankException(ErrorCode.ACCOUNT_NOT_FOUND));

        for (SavingProductAccount account : accounts) {
            LocalDate expiredDate = account.getEndDate();

            // 현재 날짜와 비교하여 만기 처리
            if (expiredDate.isEqual(LocalDate.now())) {
                Long accountId = account.getAccountId();

                Account savingAccount = accountRepository.findById(accountId)
                        .orElseThrow(() -> new BankException(ErrorCode.ACCOUNT_NOT_FOUND));

                savingAccount.setAccountStatus(AccountStatus.EXPIRED);
                log.info("계좌번호: {} 만기 처리 완료. 현재 상태: {}", savingAccount.getAccountNumber(), savingAccount.getAccountStatus());
                accountRepository.save(savingAccount);
            }
        }
    }
}
