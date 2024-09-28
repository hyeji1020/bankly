package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BankService {

    private final Map<Long, BankAccount> accountMap = new HashMap<>();

    // 계좌 추가
    public BankAccount addAccount(BankAccount bankAccount) {
        return accountMap.put(bankAccount.getAccount().getAccountNumber(), bankAccount);
    }

    // 계좌 찾기
    public BankAccount findAccountByNumber(long accountNumber) {
        return accountMap.get(accountNumber);
    }

    // 전체 계좌 개수 반환
    public int getTotalAccounts() {
        return accountMap.size();
    }

    // 저장된 계좌 정보 출력
    public void printAccounts() {
        for (Map.Entry<Long, BankAccount> entry : accountMap.entrySet()) {
            BankAccount bankAccount = entry.getValue();
            log.info("계좌번호: {}, 사용자: {}, 잔액: {}",
                    bankAccount.getAccount().getAccountNumber(),
                    bankAccount.getUser().getName(),
                    bankAccount.getAccount().getBalance());
        }
    }

    // 특정 계좌의 잔액 확인
    public int checkBalance(long accountNumber) {
        BankAccount bankAccount = findAccountByNumber(accountNumber);

        if(bankAccount == null){
            log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountNumber);
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        return bankAccount.getAccount().getBalance();
    }

    // 입금
    public Account deposit(AccountRequest accountRequest) {

        // 요청 계좌 번호 유무 확인 위해 조회
        BankAccount bankAccount = findAccountByNumber(accountRequest.getAccountNumber());

        if(bankAccount == null) {
            log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // 기존 Account 객체의 balance 업데이트
        Account updatedAccount = bankAccount.getAccount().toBuilder()
                .balance(bankAccount.getAccount().getBalance() + accountRequest.getAmount())
                .build();

        // 기존 BankAccount 객체에서 Account 필드만 변경하여 새 객체 생성
        BankAccount updatedBankAccount = new BankAccount(bankAccount.getId(), updatedAccount, bankAccount.getUser());

        // 해시맵에 반영
        accountMap.put(accountRequest.getAccountNumber(), updatedBankAccount);

        log.info("입금 후 계좌정보:{}", updatedAccount.toString());

        return updatedAccount;
    }

    // 출금
    public Account withdraw(AccountRequest accountRequest) {

        // 요청 계좌 번호 유무 확인 위해 조회
        BankAccount bankAccount = findAccountByNumber(accountRequest.getAccountNumber());

        if(bankAccount == null){
            log.warn("계좌번호: {}에 해당하는 계좌를 찾을 수 없습니다.", accountRequest.getAccountNumber());
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }else {
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

            // 해시맵에 반영
            accountMap.put(accountRequest.getAccountNumber(), updatedBankAccount);

            log.info("출금 후 계좌정보:{}", updatedAccount.toString());

        return updatedAccount;
        }
    }

    //계좌 이체
    @Transactional
    public BankAccount transfer(AccountTransferRequest transferRequest) {

        BankAccount withdrawalAccount = findAccountByNumber(transferRequest.getWithdrawalNumber());
        BankAccount transferAccount = findAccountByNumber(transferRequest.getTransferNumber());

        // 1. 출금 계좌 확인
        if (withdrawalAccount == null) {
            log.warn("계좌번호: {}에 해당하는 출금 계좌를 찾을 수 없습니다.", transferRequest.getWithdrawalNumber());
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // 2. 입금 계좌 확인
        if (transferAccount == null) {
            log.warn("계좌번호: {}에 해당하는 입금 계좌를 찾을 수 없습니다.", transferRequest.getTransferNumber());
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // 3. 내 계좌에서 출금
        AccountRequest withdrawRequest = new AccountRequest(transferRequest.getWithdrawalNumber(), transferRequest.getAmount());
        withdraw(withdrawRequest);

        // 4. 상대방 계좌에 입금
        AccountRequest depositRequest = new AccountRequest(transferRequest.getTransferNumber(), transferRequest.getAmount());
        deposit(depositRequest);

        return transferAccount;
    }

}
