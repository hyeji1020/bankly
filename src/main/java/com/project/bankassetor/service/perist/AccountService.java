package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.entity.account.save.SavingProductAccount;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.enums.AccountType;
import com.project.bankassetor.primary.model.enums.TransactionType;
import com.project.bankassetor.primary.model.request.AccountCreateRequest;
import com.project.bankassetor.primary.model.request.AccountRequest;
import com.project.bankassetor.primary.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.primary.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final BankAccountService bankAccountService;
    private final MemberService memberService;

    private final CheckingTransactionHistoryService checkingHistoryService;
    private final CheckingAccountService checkingAccountService;

    private final SavingAccountService savingAccountService;
    private final SavingProductService savingProductService;
    private final SavingProductAccountService savingProductAccountService;
    private final SavingTransactionHistoryService savingHistoryService;

    // 계좌 조회
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            log.warn("아이디 {}: 에 해당하는 계좌를 찾을 수 없습니다.", id);
            return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        });
    }

    // 계좌 조회
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // 입금
    @Transactional
    public Account deposit(AccountRequest accountRequest) {

        String accountNumber = accountRequest.getAccountNumber();

        // 요청 계좌 번호 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        if (account.getAccountStatus() == AccountStatus.ACTIVE) {

            // 입금 한도 확인
            BigDecimal depositAmount = accountRequest.getAmount();
            BigDecimal maxDepositLimit = account.getDepositLimit();
            if (depositAmount.compareTo(maxDepositLimit) > 0) {
                log.warn("입금 금액: {}이 계좌의 입금 한도: {}를 초과했습니다.", depositAmount, maxDepositLimit);
                throw new BankException(ErrorCode.DEPOSIT_LIMIT_EXCEEDED);
            }

            // 입금
            account.setBalance(account.getBalance().add(depositAmount));

            log.info("입금 금액:{}, 입금 후 계좌정보:{}", accountRequest.getAmount(), toJson(account));

            // 거래 내역 저장
            if (account.getAccountType() == AccountType.CHECKING) {
                BankAccount bankAccount = bankAccountService.findByAccountId(account.getId());
                checkingHistoryService.save(bankAccount, accountRequest.getAmount(), account.getBalance(), TransactionType.DEPOSIT.toString());
            } else if (account.getAccountType() == AccountType.SAVING) {
                SavingProductAccount savingProductAccount = savingProductAccountService.findByAccountId(account.getId());
                savingHistoryService.save(savingProductAccount, accountRequest.getAmount(), account, TransactionType.DEPOSIT.toString());
            }
        }
        return account;
    }

    // 출금
    @Transactional
    public Account withdraw(AccountRequest accountRequest) {

        // 요청 계좌 번호 확인
        Account account = accountRepository.findByAccountNumber(accountRequest.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호{}: 에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        if (account.getAccountStatus() == AccountStatus.ACTIVE) {
            // 출금 가능한 잔액 조회
            BigDecimal withdrawalAmount = accountRequest.getAmount();
            if (account.getBalance().compareTo(withdrawalAmount) < 0) {
                log.warn("계좌번호: {}에 잔액이 부족합니다. 현재 잔액: {}", accountRequest.getAccountNumber(), account.getBalance());
                throw new BalanceNotEnoughException(ErrorCode.BALANCE_NOT_ENOUGH);
            }

            // 출금
            account.setBalance(account.getBalance().subtract(withdrawalAmount));

            log.info("출금 후 계좌정보:{}", toJson(account));

            // 거래 내역 저장
            if (account.getAccountType() == AccountType.CHECKING) {
                BankAccount bankAccount = bankAccountService.findByAccountId(account.getId());
                checkingHistoryService.save(bankAccount, accountRequest.getAmount(), account.getBalance(), TransactionType.WITHDRAW.toString());
            } else if (account.getAccountType() == AccountType.SAVING) {
                SavingProductAccount savingProductAccount = savingProductAccountService.findByAccountId(account.getId());
                savingHistoryService.save(savingProductAccount, accountRequest.getAmount(), account, TransactionType.WITHDRAW.toString());
            }

        }
        return account;
    }

    /**
     * 계좌 이체 기능을 처리하는 메서드.
     * <p>
     * 1. 송금자의 계좌(fromAccountId)를 조회하여 유효성을 확인하고, 출금 금액을 반영해 잔액을 수정합니다.
     * 2. 수신자의 계좌(accountRequest.getAccountNumber())를 조회하여 유효성을 확인하고, 입금 금액을 반영해 잔액을 수정합니다.
     * 3. 거래 내역을 저장하며, 거래 유형은 "TRANSFER"로 기록합니다.
     *
     * @param fromAccountId  송금자의 계좌 ID
     * @param accountRequest 송금 요청 정보 (송금액 및 수신 계좌 번호 포함)
     * @return BankAccount 송금 후 수신자의 계좌 정보 반환
     */
    @Transactional
    public BankAccount transfer(Long fromAccountId, AccountRequest accountRequest) {

        // 송금 계좌 유효성 확인
        Account fromAccount = getAccountById(fromAccountId);

        // 입출금 금액
        BigDecimal amount = accountRequest.getAmount();

        // 송금 계좌에서 출금
        AccountRequest withdrawRequest = new AccountRequest(fromAccount.getAccountNumber(), amount);
        withdraw(withdrawRequest);

        // 수신 계좌에 입금
        deposit(accountRequest);

        BankAccount transferAccount = bankAccountService.findByAccountNumber(accountRequest.getAccountNumber());

        // 거래 내역 저장(송금 계좌 기준)
        checkingHistoryService.save(transferAccount, accountRequest.getAmount(), fromAccount.getBalance(), TransactionType.TRANSFER.toString());

        return transferAccount;
    }

    // 계좌 번호 생성 로직
    private String generateAccountNumber() {

        return accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                .orElse("1000000000");
    }

    // 당좌 계좌 생성
    public Account createAccount(Long memberId, AccountCreateRequest createRequest) {
        // 사용자 유효성 확인
        memberService.findById(memberId);

        // 계좌 번호 생성
        String newAccountNumber = generateAccountNumber();

        // 저장
        Account account = Account.builder()
                .accountNumber(newAccountNumber)
                .balance(createRequest.getInitialDeposit())
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .depositLimit(createRequest.getDepositLimit())
                .build();

        Account savedAccount = accountRepository.save(account);

        CheckingAccount checkingAccount = checkingAccountService.save(savedAccount);

        bankAccountService.save(memberId, checkingAccount.getId(), savedAccount.getId());

        return savedAccount;
    }

    // 적금 계좌 생성
    public Account createSavingAccount(Long memberId, Long savingProductId, SavingAccountCreateRequest createRequest) {
        // 사용자 유효성 확인
        memberService.findById(memberId);

        // 적금 상품 유효성 확인
        SavingProduct savingProduct = savingProductService.findById(savingProductId);

        // 계좌 번호 생성
        String newAccountNumber = generateAccountNumber();

        // 저장
        Account account = Account.builder()
                .accountNumber(newAccountNumber)
                .balance(createRequest.getInitialDeposit())
                .accountType(AccountType.SAVING)
                .accountStatus(AccountStatus.ACTIVE)
                .depositLimit(createRequest.getDepositLimit())
                .build();

        Account savedAccount = accountRepository.save(account);
        SavingAccount savingAccount = savingAccountService.save(savedAccount.getId());

        savingProductAccountService.save(memberId, savingProductId, savingAccount.getId(),
                savingProduct, createRequest.getMonthlyDeposit(), savedAccount);

        return savedAccount;
    }

    public List<Account> findCheckByMemberId(long memberId) {
        return accountRepository.findCheckByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    public List<Account> findSaveByMemberId(long memberId) {
        return accountRepository.findSaveByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }
}
