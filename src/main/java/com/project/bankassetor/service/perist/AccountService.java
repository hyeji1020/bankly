package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.enums.AccountType;
import com.project.bankassetor.model.request.AccountCreateRequest;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionHistoryService historyService;
    private final BankAccountService bankAccountService;
    private final UserService userService;

    // 계좌 조회
    public Account getAccountById(long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> {
            log.warn("아이디 {}: 에 해당하는 계좌를 찾을 수 없습니다.", id);
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        });
        return account;
    }

    // 계좌 조회
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // 특정 계좌의 잔액 확인
    public int checkBalance(Long accountNumber) {

        // 요청 계좌 번호 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountNumber);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        return account.getBalance();
    }

    // 입금
    @Transactional
    public Account deposit(AccountRequest accountRequest) {

        // 요청 계좌 번호 확인
        Account account = accountRepository.findByAccountNumber(accountRequest.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        // 입금 금액
        int amount = accountRequest.getAmount();

        // 입금
        accountRepository.depositUpdateBalance(account.getId(), amount);
        log.info("입금 후 계좌정보:{}", account.toString());

        BankAccount bankAccount = bankAccountService.findByAccountId(account.getId());

        historyService.save(bankAccount, accountRequest.getAmount(), account.getBalance());

        return account;
    }

    // 출금
    @Transactional
    public Account withdraw(AccountRequest accountRequest) {

        // 요청 계좌 번호 확인
        Account account = accountRepository.findByAccountNumber(accountRequest.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호{}: 에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        BankAccount bankAccount = bankAccountService.findByAccountId(account.getId());

        // 출금 가능한 잔액 조회
        if(account.getBalance() < accountRequest.getAmount()){
            log.warn("계좌번호: {}에 잔액이 부족합니다.", accountRequest.getAccountNumber());
            throw new BalanceNotEnoughException(ErrorCode.BALANCE_NOT_ENOUGH);
        }

        // 출금 금액
        int amount = accountRequest.getAmount();

        accountRepository.withdrawUpdateBalance(account.getId(), amount);
        log.info("출금 후 계좌정보:{}", account.toString());

        historyService.save(bankAccount, accountRequest.getAmount(), account.getBalance());

        return account;
    }

    //계좌 이체
    @Transactional
    public BankAccount transfer(AccountTransferRequest transferRequest) {

        // 내 계좌에서 출금
        AccountRequest withdrawRequest = new AccountRequest(transferRequest.getWithdrawalNumber(), transferRequest.getAmount());
        withdraw(withdrawRequest);

        // 상대방 계좌에 입금
        AccountRequest depositRequest = new AccountRequest(transferRequest.getTransferNumber(), transferRequest.getAmount());
        deposit(depositRequest);

        BankAccount transferAccount = bankAccountService.findByAccountNumber(transferRequest.getTransferNumber());

        return transferAccount;
    }

    // 계좌 번호 생성 로직
    private long generateAccountNumber() {
        Random random = new Random();
        // 100000부터 999999까지의 숫자 생성 (7자리)
        long accountNumber = 1000000 + random.nextInt(900000);
        return accountNumber;
    }

    // 계좌 생성
    public Account createAccount(Long userId, AccountCreateRequest createRequest) {
        // 사용자 유효성 확인
        userService.findById(userId);

        // 계좌 번호 생성
        long newAccountNumber = generateAccountNumber();

        // 저장
        Account account = Account.builder()
                .accountNumber(newAccountNumber)
                .balance(createRequest.getInitialDeposit())
                .accountType(AccountType.valueOf(createRequest.getAccountType()))
                .build();

        Account savedAccount = accountRepository.save(account);

        bankAccountService.createBankAccount(userId, savedAccount.getId());

        return savedAccount;
    }
}
