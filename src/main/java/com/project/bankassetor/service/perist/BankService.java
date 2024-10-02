package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.exception.HistoryNotFoundException;
import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.entity.TransactionHistory;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.repository.BankAccountRepository;
import com.project.bankassetor.repository.TransactionHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;


    // 특정 계좌의 잔액 확인
    public int checkBalance(long accountNumber) {

        // 요청 계좌 번호 확인
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountNumber);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        return bankAccount.getAccount().getBalance();
    }

    // 입금
    @Transactional
    public Account deposit(AccountRequest accountRequest) {

        // 요청 계좌 번호 확인
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountRequest.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        // 기존 Account 객체의 balance 업데이트
        Account updatedAccount = bankAccount.getAccount().toBuilder()
                .balance(bankAccount.getAccount().getBalance() + accountRequest.getAmount())
                .build();

        // 기존 BankAccount 객체에서 Account 필드만 변경하여 새 객체 생성
        BankAccount updatedBankAccount = new BankAccount(bankAccount.getId(), updatedAccount, bankAccount.getUser());

        // BalanceHistory 생성
        TransactionHistory toHistory = TransactionHistory.builder()
                .bankAccount(bankAccount)
                .transactionTime(LocalDateTime.now())
                .transactionAmount(accountRequest.getAmount())
                .balanceBefore(bankAccount.getAccount().getBalance())
                .balanceAfter(updatedAccount.getBalance())
                .build();

        // 변경사항 저장
        bankAccountRepository.save(updatedBankAccount);
        log.info("입금 후 계좌정보:{}", updatedAccount.toString());

        transactionHistoryRepository.save(toHistory);
        log.info("거래내역 정보:{}", toHistory.toString());

        return updatedAccount;
    }

    // 출금
    @Transactional
    public Account withdraw(AccountRequest accountRequest) {

        // 요청 계좌 번호 유무 확인 위해 조회
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountRequest.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        // 출금 가능한 계좌 잔액 조회
        if(bankAccount.getAccount().getBalance() < accountRequest.getAmount()){
            log.warn("계좌번호: {}에 잔액이 부족합니다.", accountRequest.getAccountNumber());
            throw new BalanceNotEnoughException(ErrorCode.BALANCE_NOT_ENOUGH);
        }

        // 기존 Account 객체의 balance 업데이트
        Account updatedAccount = bankAccount.getAccount().toBuilder()
                .balance(bankAccount.getAccount().getBalance() - accountRequest.getAmount())
                .build();

        // 기존 BankAccount 객체에서 Account 필드만 변경하여 새 객체 생성
        BankAccount updatedBankAccount = new BankAccount(bankAccount.getId(), updatedAccount, bankAccount.getUser());

        // BalanceHistory 생성
        TransactionHistory toHistory = TransactionHistory.builder()
                .bankAccount(bankAccount)
                .transactionTime(LocalDateTime.now())
                .transactionAmount(accountRequest.getAmount())
                .balanceBefore(bankAccount.getAccount().getBalance())
                .balanceAfter(updatedAccount.getBalance())
                .build();

        // 변경사항 저장
        bankAccountRepository.save(updatedBankAccount);
        log.info("출금 후 계좌정보:{}", updatedAccount.toString());

        transactionHistoryRepository.save(toHistory);
        log.info("거래내역 정보:{}", toHistory.toString());

        return updatedAccount;

    }

    //계좌 이체
    @Transactional
    public BankAccount transfer(AccountTransferRequest transferRequest) {

        // 1. 출금 계좌 확인
        BankAccount withdrawalAccount = bankAccountRepository.findByAccountNumber(transferRequest.getWithdrawalNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 출금 계좌를 찾을 수 없습니다.", transferRequest.getWithdrawalNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        // 2. 입금 계좌 확인
        BankAccount transferAccount = bankAccountRepository.findByAccountNumber(transferRequest.getTransferNumber())
                .orElseThrow(() -> {
                    log.warn("계좌번호: {}에 해당하는 입금 계좌를 찾을 수 없습니다.", transferRequest.getTransferNumber());
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        // 3. 내 계좌에서 출금
        AccountRequest withdrawRequest = new AccountRequest(transferRequest.getWithdrawalNumber(), transferRequest.getAmount());
        withdraw(withdrawRequest);

        // 4. 상대방 계좌에 입금
        AccountRequest depositRequest = new AccountRequest(transferRequest.getTransferNumber(), transferRequest.getAmount());
        deposit(depositRequest);

        return transferAccount;
    }

    // 거래 내역 확인
    public List<TransactionHistory> findBalanceHistory(Long accountId) {

        BankAccount bankAccount = bankAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("계좌번호 아이디: {}에 해당하는 계좌를 찾을 수 없습니다.", accountId);
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                });

        List<TransactionHistory> findHistory = transactionHistoryRepository.findHistoriesByAccountId(accountId);

        return findHistory;
    }

}
