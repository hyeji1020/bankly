package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.BalanceNotEnoughException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.entity.User;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.repository.AccountRepository;
import com.project.bankassetor.repository.BankAccountRepository;
import com.project.bankassetor.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionHistoryService historyService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @BeforeEach
    public void setUp() {

        transactionHistoryRepository.deleteAllInBatch();
        bankAccountRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();

        // 10개의 계좌와 유저 데이터를 초기화
        for (int i = 1; i <= 3; i++) {
            long accountId = i;
            long accountNumber = 100000 + i;
            int balance = i * 1000;
            Account account = new Account(accountId, accountNumber, balance);

            long userId = i;
            String userName = "User" + i;
            User user = new User(userId, userName);

            System.out.println(account);
            BankAccount bankAccount = new BankAccount(accountId, account, user);
            System.out.println(bankAccount.getAccount());
            bankAccountRepository.save(bankAccount);
        }

    }

    @Test
    @DisplayName("모든 계좌 목록 가져오기")
    public void test_Get_Accounts() {
        List<Account> list = accountService.getAccounts();
        System.out.println(list.getFirst().getAccountNumber());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("특정 계좌 잔액 올바른지 확인")
    public void test_Check_Balance() {
        long accountNumber = 100001;
        int expectedBalance = 1000;
        assertEquals(expectedBalance, accountService.checkBalance(accountNumber));
    }

    @Test
    @DisplayName("출금 성공 테스트")
    public void test_Withdraw_Success() {
        long accountNumber = 100003;
        int amount = 1000;
        AccountRequest accountRequest = new AccountRequest(accountNumber, amount);

        accountService.withdraw(accountRequest);

        assertEquals(2000, accountService.checkBalance(accountNumber));  // 기존 잔액 3000 - 1000
    }

    @Test
    @DisplayName("출금 시 계좌를 찾지 못했을 때 예외 발생 테스트")
    public void test_Withdraw_Account_Not_Found() {
        // given
        long invalidAccountNumber = 999999;  // 존재하지 않는 계좌번호
        int amount = 1000;
        AccountRequest accountRequest = new AccountRequest(invalidAccountNumber, amount);

        // when & then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.withdraw(accountRequest);
        });

        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getDefaultMessage(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getStatus());
    }

    @Test
    @DisplayName("출금 시 잔액 부족 예외 테스트")
    public void test_Withdraw_Balance_Not_Enough() {
        // given
        long accountNumber = 100003;  // 존재하는 계좌
        int amount = 5000;  // 계좌 잔액보다 많은 금액

        AccountRequest accountRequest = new AccountRequest(accountNumber, amount);

        // when & then
        BalanceNotEnoughException exception = assertThrows(BalanceNotEnoughException.class, () -> {
            accountService.withdraw(accountRequest);
        });

        assertEquals(ErrorCode.BALANCE_NOT_ENOUGH.getDefaultMessage(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode().getStatus());
    }

    @Test
    @DisplayName("입금 성공 테스트")
    public void test_Deposit_Success() {
        // given
        long accountNumber = 100003;
        int amount = 1000;
        AccountRequest accountRequest = new AccountRequest(accountNumber, amount);

        // when
        accountService.deposit(accountRequest);

        // then
        assertEquals(4000, accountService.checkBalance(accountNumber));  // 기존 잔액 3000 + 1000
    }

    @Test
    @DisplayName("입금 시 계좌를 찾지 못했을 때 예외 발생 테스트")
    public void test_Deposit_Account_Not_Found() {
        // given
        long invalidAccountNumber = 999999;  // 존재하지 않는 계좌번호
        int amount = 1000;
        AccountRequest accountRequest = new AccountRequest(invalidAccountNumber, amount);

        // when & then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit(accountRequest);
        });

        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getDefaultMessage(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getStatus());
    }

    @Test
    @DisplayName("계좌 이체 성공 테스트")
    public void test_Transfer_Success() {
        long withdrawalNumber = 100001; // 출금 계좌
        long transferNumber = 100002;   // 이체 계좌
        int amount = 500;   // 이체 금액
        AccountTransferRequest transferRequest = new AccountTransferRequest(withdrawalNumber, transferNumber, amount);

        accountService.transfer(transferRequest);

        assertEquals(500, accountService.checkBalance(withdrawalNumber));  // 기존 잔액 1000 - 500
        assertEquals(2500, accountService.checkBalance(transferNumber));  // 기존 잔액 2000 + 500
    }

    @Test
    @DisplayName("계좌 이체 시 출금 계좌를 찾지 못했을 때 예외 발생 테스트")
    public void test_Transfer_Withdrawal_Account_Not_Found() {
        // given
        long invalid_WithdrawalAccount = 999999;  // 존재하지 않는 출금 계좌
        long invalid_TransferAccount = 100002;  // 이체 계좌
        int amount = 1000;
        AccountTransferRequest accountTransferRequest = new AccountTransferRequest(invalid_WithdrawalAccount, invalid_TransferAccount, amount);

        // when & then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(accountTransferRequest);
        });

        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getDefaultMessage(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getStatus());
    }

    @Test
    @DisplayName("계좌 이체 시 출금 계좌를 찾지 못했을 때 예외 발생 테스트")
    public void test_Transfer_transfer_Account_Not_Found() {
        // given
        long invalid_WithdrawalAccount = 100002;  // 존재하지 않는 출금 계좌
        long invalid_TransferAccount = 999999;  // 이체 계좌
        int amount = 1000;
        AccountTransferRequest accountTransferRequest = new AccountTransferRequest(invalid_WithdrawalAccount, invalid_TransferAccount, amount);

        // when & then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(accountTransferRequest);
        });

        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getDefaultMessage(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getStatus());
    }

//    @Test
//    @DisplayName("거래 내역 확인 성공 테스트")
//    public void test_Transaction_History_Get_Success() {
//        // given
//        long accountId = 2L;
//        long accountNumber = 100002;
//        int amount = 1000;
//        AccountRequest accountRequest = new AccountRequest(accountNumber, amount);
//
//        // 거래 내역 만들기(2개)
//        accountService.deposit(accountRequest);
//        accountService.withdraw(accountRequest);
//
//        // when
//        List<TransactionHistory> result = historyService.findBalanceHistory(accountId);
//
//        // then
//        assertEquals(2, result.size());
//    }

}