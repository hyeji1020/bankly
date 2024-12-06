package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.project.bankassetor.utils.Utils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final CheckingAccountService checkingAccountService;
    private final MemberService memberService;

    private final CheckingTransactionHistoryService checkingHistoryService;
    private final SavingProductService savingProductService;
    private final SavingAccountService savingAccountService;
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
        Account account = findByAccountNumber(accountNumber);

        if (account.getStatus() == AccountStatus.active) {

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
            if (account.getType() == AccountType.checking) {
                CheckingAccount checkingAccount = checkingAccountService.findByAccountId(account.getId());
                checkingHistoryService.save(checkingAccount, accountRequest.getAmount(), account.getBalance(), TransactionType.deposit.toString());
            } else if (account.getType() == AccountType.saving) {
                SavingAccount savingProductAccount = savingAccountService.findByAccountId(account.getId());
                savingHistoryService.save(savingProductAccount, accountRequest.getAmount(), account, TransactionType.deposit.toString());
            }
        }
        return account;
    }

    // 출금
    @Transactional
    public Account withdraw(AccountRequest accountRequest) {

        // 요청 계좌 번호 확인
        Account account = findByAccountNumber(accountRequest.getAccountNumber());

        if (account.getStatus() == AccountStatus.active) {
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
            if (account.getType() == AccountType.checking) {
                CheckingAccount bankAccount = checkingAccountService.findByAccountId(account.getId());
                checkingHistoryService.save(bankAccount, accountRequest.getAmount(), account.getBalance(), TransactionType.withdraw.toString());
            } else if (account.getType() == AccountType.saving) {
                SavingAccount savingProductAccount = savingAccountService.findByAccountId(account.getId());
                savingHistoryService.save(savingProductAccount, accountRequest.getAmount(), account, TransactionType.withdraw.toString());
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
    public Account transfer(Long fromAccountId, AccountRequest accountRequest) {

        // 출금 계좌
        Account fromAccount = getAccountById(fromAccountId);

        // 송금 계좌
        Account toAccount = findByAccountNumber(accountRequest.getAccountNumber());

        // 출금
        AccountRequest withdrawRequest = new AccountRequest(fromAccount.getAccountNumber(), accountRequest.getAmount());
        withdraw(withdrawRequest);

        // 수신 계좌에 입금
        deposit(accountRequest);

        Member member = findMemberByAccountType(toAccount);
        log.info("{}님에게 {}원 입금합니다. 받는계좌:{}",
                member.getName(), accountRequest.getAmount(), toAccount.getAccountNumber());

        return toAccount;
    }

    // 계좌 번호 생성 로직
    private String generateAccountNumber() {
        return accountRepository.findFirstByOrderByIdDesc()
                .map(account -> {
                    // 기존 계좌번호를 숫자로 간주하고 +1
                    String accountNumber = account.getAccountNumber();
                    try {
                        long nextNumber = Long.parseLong(accountNumber) + 1;
                        return String.valueOf(nextNumber);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("계좌번호가 숫자가 아닙니다: " + accountNumber, e);
                    }
                })
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
                .type(AccountType.checking)
                .status(AccountStatus.active)
                .depositLimit(createRequest.getDepositLimit())
                .build();

        Account savedAccount = accountRepository.save(account);

        CheckingAccount checkingAccount = checkingAccountService.save(memberId, savedAccount.getId());

        log.info("입출금 계좌 생성 성공 : {}", toJson(checkingAccount));

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

        // 납입 한도 체크
        if (createRequest.getMonthlyDeposit().compareTo(savingProduct.getSavingLimit()) > 0) {
            log.error("{}: 입력한 월 납입액이 {}: 월 한도보다 큽니다.", createRequest.getMonthlyDeposit(), savingProduct.getSavingLimit());
            throw new BankException(ErrorCode.OVER_SAVING_LIMIT);
        }

        // 저장
        Account account = Account.builder()
                .accountNumber(newAccountNumber)
                .balance(BigDecimal.ZERO)
                .type(AccountType.saving)
                .status(AccountStatus.active)
                .depositLimit(savingProduct.getSavingLimit())
                .build();

        Account savedAccount = accountRepository.save(account);

        SavingAccount savingProductAccount = savingAccountService.save(memberId, savingProduct, createRequest.getMonthlyDeposit(), savedAccount);

        log.info("적금 계좌 생성 성공 : {}", toJson(savingProductAccount));

        return savedAccount;
    }

    public List<Account> findCheckByMemberId(long memberId) {
        return accountRepository.findCheckByMemberId(memberId)
                .orElseThrow(() -> {
                    log.error("{}에 해당하는 입출금 계좌가 없습니다.", memberId);
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }

    public List<Account> findSaveByMemberId(long memberId) {
        return accountRepository.findSaveByMemberId(memberId)
                .orElseThrow(() -> {
                    log.error("{}에 해당하는 적금 계좌가 없습니다.", memberId);
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.error("{}에 해당하는 계좌가 없습니다.", accountNumber);
                    return new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });
    }

    // 입금 계좌 유형에 따라 수신자 정보 가져오는 메서드
    private Member findMemberByAccountType(Account toAccount) {
        return toAccount.getType() == AccountType.checking
                ? memberService.findCheckByAccountId(toAccount.getId())
                : memberService.findSaveByAccountId(toAccount.getId());
    }

    public void saveAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    public long count() {
        return accountRepository.count();
    }

    public Page<Account> findAllByMemberId(String accountType, String keyword, long memberId, Pageable pageable) {
        return accountRepository.findAllByMemberId(accountType, keyword, memberId, pageable);
    }
}
