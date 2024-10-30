package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingProductAccountService {

    private final SavingProductAccountRepository productAccountRepository;
    private final AccountRepository accountRepository;

    public SavingProductAccount save(Long userId, Long savingProductId, Long savingAccountId,
                                     SavingProduct savingProduct, BigDecimal monthlyDeposit, Account account){

        SavingProductAccount savingProductAccount = SavingProductAccount.builder()
                .savingProductId(savingProductId)
                .savingAccountId(savingAccountId)
                .userId(userId)
                .monthlyDeposit(monthlyDeposit)
                .startDate(account.getCreatedAt().toLocalDate())
                .endDate(account.getCreatedAt().toLocalDate()
                        .plusMonths(savingProduct.getSavingDuration().getDurationInMonths()))
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

    /**
     * 만기가 도래한 적금 계좌들을 조회하여 만기 상태로 업데이트하는 메서드
     *
     * 현재 날짜 이전에 만기가 도래한 모든 `SavingProductAccount` 계좌를 조회한 후,
     * 각 계좌에 해당하는 `Account` 객체를 가져와 만기 상태를 `EXPIRED`로 변경합니다.
     * 모든 업데이트가 완료된 계좌들을 한 번에 `saveAll`을 통해 저장하여 효율적으로
     * DB에 반영합니다.
     */
    @Transactional
    public void expireMaturedAccounts() {
        // 1. 만기 도래한 계좌 조회
        List<SavingProductAccount> accounts = productAccountRepository.findAllByEndDateBefore(LocalDate.now());

        Map<Long, Account> expiredAccountMap = new HashMap<>();

        for (SavingProductAccount account : accounts) {
            Long accountId = account.getAccountId();

            Account savingAccount = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BankException(ErrorCode.ACCOUNT_NOT_FOUND));

            // 2. 각 계좌의 상태를 만기(EXPIRED)로 업데이트
            savingAccount.setAccountStatus(AccountStatus.EXPIRED);
            log.info("계좌번호: {} 만기 처리 완료. 현재 상태: {}", savingAccount.getAccountNumber(), savingAccount.getAccountStatus());

            // 3. 업데이트된 계좌 정보를 Map에 저장하여 중복 방지
            expiredAccountMap.put(accountId, savingAccount);
        }

        // Map의 모든 값을 리스트로 변환 후 한 번에 저장
        accountRepository.saveAll(new ArrayList<>(expiredAccountMap.values()));

    }
}
