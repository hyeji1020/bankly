package com.project.bankassetor.api;

import com.project.bankassetor.model.request.AccountCreateRequest;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.model.response.*;
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
    @PutMapping("/transfer")
    public ResultResponse<AccountTransferResponse> transfer(@Valid @RequestBody AccountTransferRequest transferRequest) {
        AccountTransferResponse response = bankFrontService.transfer(transferRequest);
        return new ResultResponse<>(response);
    }

    // 거래 내역 확인
    @GetMapping("/{accountId}/balance-history")
    public ResultResponse<List<TransactionHistoryResponse>> findBalanceHistory(@PathVariable Long accountId) {
        List<TransactionHistoryResponse> response = bankFrontService.findBalanceHistory(accountId);
        return new ResultResponse<>(response);
    }
    
    // 당좌 계좌 생성
    @PostMapping("/{userId}")
    public ResultResponse<AccountCreateResponse> createCheckingAccount(@PathVariable Long userId, @Valid @RequestBody AccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createAccount(userId, createRequest);
        return new ResultResponse<>(response);
    }


    // 적금 계좌 생성
    @PostMapping("/{userId}/{savingProductId}")
    public ResultResponse<AccountCreateResponse> createSavingAccount(@PathVariable Long userId,@PathVariable Long savingProductId,
                                                                     @Valid @RequestBody SavingAccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createSavingAccount(userId, savingProductId, createRequest);
        return new ResultResponse<>(response);
    }

}
