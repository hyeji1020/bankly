package com.project.bankassetor.api;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.request.AccountCreateRequest;
import com.project.bankassetor.primary.model.request.AccountRequest;
import com.project.bankassetor.primary.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.primary.model.response.*;
import com.project.bankassetor.service.front.BankFrontService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bank")
public class BankApi {

    private final BankFrontService bankFrontService;

    // 입금
    @PutMapping("/deposit")
    public ResultResponse<AccountResponse> deposit(@Valid @RequestBody AccountRequest accountRequest) {
        AccountResponse response = bankFrontService.deposit(accountRequest);
        return new ResultResponse<>(response);
    }

    // 출금
    @PutMapping("/withdrawal")
    public ResultResponse<AccountResponse> withdrawal(@Valid @RequestBody AccountRequest accountRequest) {
        AccountResponse response = bankFrontService.withdraw(accountRequest);
        return new ResultResponse<>(response);
    }

    // 계좌 이체
    @PutMapping("/{fromAccountId}/transfer")
    public ResultResponse<AccountTransferResponse> transfer(@PathVariable Long fromAccountId, @Valid @RequestBody AccountRequest accountRequest) {
        AccountTransferResponse response = bankFrontService.transfer(fromAccountId, accountRequest);
        return new ResultResponse<>(response);
    }

    // 거래 내역 확인
    @GetMapping("/{accountId}/balance-history")
    public ResultResponse<List<CheckingTransactionHistoryResponse>> findBalanceHistory(@PathVariable Long accountId) {
        List<CheckingTransactionHistoryResponse> response = bankFrontService.findBalanceHistory(accountId);
        return new ResultResponse<>(response);
    }
    
    // 당좌 계좌 생성
    @PostMapping("/checking-accounts")
    public ResultResponse<AccountCreateResponse> createCheckingAccount(@Authed Member member, @Valid @RequestBody AccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createAccount(member.getId(), createRequest);
        return new ResultResponse<>(response);
    }

    // 적금 계좌 생성
    @PostMapping("/saving-products/{savingProductId}/accounts")
    public ResultResponse<AccountCreateResponse> createSavingAccount(@Authed Member member,@PathVariable Long savingProductId,
                                                                     @Valid @RequestBody SavingAccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createSavingAccount(member.getId(), savingProductId, createRequest);
        return new ResultResponse<>(response);
    }

    // 나의 입출금 계좌 목록
    @GetMapping("/my-checking-accounts")
    public ResultResponse<List<AccountResponse>> getMyCheckAccounts(@Authed Member member) {
        List<AccountResponse> checkAccounts = bankFrontService.getMyCheckAccounts(member.getId());

        return new ResultResponse<>(checkAccounts);
    }

    // 나의 적금 계좌 목록
    @GetMapping("/my-saving-accounts")
    public ResultResponse<List<AccountResponse>> getMySaveAccounts(@Authed Member member) {
        List<AccountResponse> savingAccounts = bankFrontService.getMySaveAccounts(member.getId());

        return new ResultResponse<>(savingAccounts);
    }

    // 입출금 거래내역
    @GetMapping("/checking-transaction-history/{accountId}")
    public ResultResponse<List<CheckingTransactionHistoryResponse>> getMyCheckTransactionHistory(@Authed Member member, @PathVariable long accountId) {
        List<CheckingTransactionHistoryResponse> response = bankFrontService.getCheckTransactionHistory(accountId);

        return new ResultResponse<>(response);
    }

    // 적금 거래내역
    @GetMapping("/saving-transaction-history/{accountId}")
    public ResultResponse<List<SavingTransactionHistoryResponse>> getMySaveTransactionHistory(@Authed Member member, @PathVariable long accountId) {
        List<SavingTransactionHistoryResponse> response = bankFrontService.getSaveTransactionHistory(accountId);

        return new ResultResponse<>(response);
    }

    // 적금 상품 목록
    @GetMapping("/saving-products")
    public ResultResponse<List<SavingProductResponse>> getSavingProducts() {
        List<SavingProductResponse> response = bankFrontService.getSavingProducts();

        return new ResultResponse<>(response);
    }

    // 적금 상품 조회
    @GetMapping("/saving-products-detail/{savingProductId}")
    public ResultResponse<SavingProductResponse> getSavingProducts(@PathVariable long savingProductId) {
        SavingProductResponse response = bankFrontService.getSavingProduct(savingProductId);

        return new ResultResponse<>(response);
    }
}
