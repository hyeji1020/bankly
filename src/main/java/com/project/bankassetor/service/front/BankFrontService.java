package com.project.bankassetor.service.front;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.check.BankAccount;
import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.primary.model.request.AccountCreateRequest;
import com.project.bankassetor.primary.model.request.AccountRequest;
import com.project.bankassetor.primary.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.primary.model.response.*;
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
    public AccountTransferResponse transfer(Long fromAccountId, AccountRequest accountRequest) {

        final BankAccount transferAccount = accountService.transfer(fromAccountId, accountRequest);

        // 응답 DTO 반환
        return new AccountTransferResponse(
                transferAccount.getMemberId(),
                transferAccount.getCheckingAccountId(),
                accountRequest.getAmount()
        );

    }

    // 거래 내역 확인
    public List<TransactionHistoryResponse> findBalanceHistory(Long accountId) {

        final List<CheckingTransactionHistory> balanceHistory = historyService.findBalanceHistory(accountId);

        return TransactionHistoryResponse.of(balanceHistory);
    }

    // 계좌 생성
    public AccountCreateResponse createAccount(Long memberId, AccountCreateRequest createRequest) {

        final Account account = accountService.createAccount(memberId, createRequest);

        return AccountCreateResponse.of(account);
    }

    public AccountCreateResponse createSavingAccount(Long memberId, Long savingProductId, SavingAccountCreateRequest createRequest) {

        final Account account = accountService.createSavingAccount(memberId, savingProductId, createRequest);

        return AccountCreateResponse.of(account);
    }

    // 나의 당좌 계좌 목록 확인
    public List<AccountResponse> getMyCheckAccounts(long memberId) {
        final List<Account> accounts = accountService.findCheckByMemberId(memberId);

        return AccountResponse.of(accounts);
    }

    // 나의 적금 계좌 목록 확인
    public List<AccountResponse> getMySaveAccounts(long memberId) {
        final List<Account> accounts = accountService.findSaveByMemberId(memberId);

        return AccountResponse.of(accounts);
    }

}
