package com.project.bankassetor.service.front;

import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.account.check.BankAccount;
import com.project.bankassetor.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.model.entity.account.save.SavingAccount;
import com.project.bankassetor.model.request.AccountCreateRequest;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.model.response.AccountCreateResponse;
import com.project.bankassetor.model.response.AccountResponse;
import com.project.bankassetor.model.response.AccountTransferResponse;
import com.project.bankassetor.model.response.TransactionHistoryResponse;
import com.project.bankassetor.service.perist.AccountService;
import com.project.bankassetor.service.perist.BankAccountService;
import com.project.bankassetor.service.perist.CheckingTransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankAccountService bankAccountService;
    private final CheckingTransactionHistoryService historyService;
    private final AccountService accountService;

    // 입금
    public AccountResponse deposit(AccountRequest accountRequest) {

        final Account account = accountService.deposit(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 출금
    public AccountResponse withdraw(AccountRequest accountRequest) {

        final Account account = accountService.withdraw(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 계좌 이체
    public AccountTransferResponse transfer(AccountTransferRequest transferRequest) {

        final BankAccount transferAccount = accountService.transfer(transferRequest);

        // 응답 DTO 반환
        return new AccountTransferResponse(
                transferAccount.getUserId(),
                transferAccount.getAccountId(),
                transferRequest.getAmount()
        );

    }

    // 거래 내역 확인
    public List<TransactionHistoryResponse> findBalanceHistory(Long accountId) {

        final List<CheckingTransactionHistory> balanceHistory = historyService.findBalanceHistory(accountId);

        return TransactionHistoryResponse.of(balanceHistory);
    }

    // 계좌 생성
    public AccountCreateResponse createAccount(Long userId, AccountCreateRequest createRequest) {

        final Account account = accountService.createAccount(userId, createRequest);

        return AccountCreateResponse.of(account);
    }

    public AccountCreateResponse createSavingAccount(Long userId, Long savingProductId, SavingAccountCreateRequest createRequest) {

        final Account account = accountService.createSavingAccount(userId, savingProductId, createRequest);

        return AccountCreateResponse.of(account);
    }
}
