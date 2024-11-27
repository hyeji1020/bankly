package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.response.InterestCalcResponse;
import com.project.bankassetor.primary.model.response.SavingTransactionHistoryResponse;
import com.project.bankassetor.primary.model.response.TerminateResponse;
import com.project.bankassetor.primary.repository.AccountRepository;
import com.project.bankassetor.primary.repository.SavingAccountRepository;
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

import static com.project.bankassetor.service.perist.InterestCalculationService.afterTaxInterest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingAccountService {

    private final SavingAccountRepository savingAccountRepository;
    private final AccountRepository accountRepository;
    private final InterestCalculationService calculationService;
    private final SavingProductService savingProductService;
    private final SavingTransactionHistoryService historyService;

    public SavingAccount save(Long memberId, SavingProduct savingProduct, BigDecimal monthlyDeposit, Account account){

        SavingAccount savingProductAccount = SavingAccount.builder()
                .savingProductId(savingProduct.getId())
                .accountId(account.getId())
                .memberId(memberId)
                .monthlyDeposit(monthlyDeposit)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(savingProduct.getDurationInMonths()))
                .build();

        return savingAccountRepository.save(savingProductAccount);
    }

    public SavingAccount findByAccountId(Long accountId) {

        return savingAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("{}: 아이디에 해당하는 적금 계좌를 찾을 수 없습니다.", accountId);
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }

    /**
     * 만기가 도래한 적금 계좌들을 조회하여 만기 상태로 업데이트하는 메서드
     * 현재 날짜 이전에 만기가 도래한 모든 `SavingProductAccount` 계좌를 조회한 후,
     * 각 계좌에 해당하는 `Account` 객체를 가져와 만기 상태를 `EXPIRED`로 변경합니다.
     * 모든 업데이트가 완료된 계좌들을 한 번에 `saveAll`을 통해 저장하여 효율적으로
     * DB에 반영합니다.
     **/
    @Transactional
    public void expireMaturedAccounts() {

        // 만기 도래한 계좌 조회
        List<SavingAccount> accounts = savingAccountRepository.findAllByEndDateBefore(LocalDate.now());

        Map<Long, Account> expiredAccountMap = new HashMap<>();

        for (SavingAccount savingAccount : accounts) {
            Long accountId = savingAccount.getAccountId();

            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> {
                        log.error("만기 도래 계좌 조회 중 : {}에 해당하는 계좌를 찾을 수 없습니다.", accountId);
                        return new BankException(ErrorCode.ACCOUNT_NOT_FOUND);
                    });

            if(account.getStatus() == AccountStatus.active){

                // 월 납입액, 납입회차
                BigDecimal monthlyDeposit = savingAccount.getMonthlyDeposit();
                int depositCount = savingAccount.getCurrentDepositCount();

                // 각 적금 상품의 이자
                SavingProduct savingProduct = savingProductService.findById(savingAccount.getSavingProductId());
                BigDecimal interestRate = savingProduct.getInterestRate();

                // 만기 금액 계산
                BigDecimal maturityAmount = calculationService.maturityAmount(monthlyDeposit, depositCount, interestRate);

                account.setBalance(maturityAmount);
                log.info("계좌번호: {} 지급액: {}", account.getAccountNumber(), maturityAmount);

                // 각 계좌의 상태를 만기(expired)로 업데이트
                account.setStatus(AccountStatus.expired);
                log.info("계좌번호: {} 만기 처리 완료. 현재 상태: {}", account.getAccountNumber(), account.getStatus());

                // 업데이트된 계좌 정보를 Map에 저장하여 중복 방지
                expiredAccountMap.put(accountId, account);
            }
        }
        // Map의 모든 값을 리스트로 변환 후 한 번에 저장
        log.info("만기 적금 리스트 저장 중");
        accountRepository.saveAll(new ArrayList<>(expiredAccountMap.values()));

    }

    public void saveAll(List<SavingAccount> savingAccounts) {
        savingAccountRepository.saveAll(savingAccounts);
    }

    public long count() {
        return savingAccountRepository.count();
    }

    @Transactional
    public SavingTransactionHistory terminateSavingAccount(long accountId, long memberId) {

        SavingAccount savingAccount = findByAccountId(accountId);

        if (!savingAccount.getMemberId().equals(memberId)) {
            log.error("적금 상품 중도 해지 중 : 계좌 소유자가 아닙니다. 요청자: {}, 계좌 소유자: {}", memberId, savingAccount.getMemberId());
            throw new BankException(ErrorCode.INVALID_ACCOUNT_OWNER);
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("적금 상품 중도 해지 중 : {}에 해당하는 계좌를 찾을 수 없습니다.", accountId);
                    return new BankException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        if (account.getStatus() == AccountStatus.close) {
            throw new BankException(ErrorCode.ACCOUNT_ALREADY_TERMINATE);
        }

        SavingProduct savingProduct = savingProductService.findById(savingAccount.getSavingProductId());

        BigDecimal monthlyDeposit = savingAccount.getMonthlyDeposit();
        int depositCount = savingAccount.getCurrentDepositCount();
        BigDecimal interestRate = savingProduct.getTermInterestRate();
        LocalDate startDate = savingAccount.getStartDate();

        BigDecimal terminateAmount = calculationService.terminateAmount(monthlyDeposit, depositCount, interestRate, startDate);

        account.setStatus(AccountStatus.close);
        account.setBalance(terminateAmount);
        accountRepository.save(account);
        log.info("계좌번호: {}, 계좌 상태 {}, 지급액 {} 원", account.getAccountNumber(), account.getStatus(), terminateAmount);

        return historyService.savePenalty(accountId, savingAccount, terminateAmount);

    }
}
