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
import org.springframework.ui.Model;
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
    @PostMapping("/{memberId}")
    public ResultResponse<AccountCreateResponse> createCheckingAccount(@PathVariable Long memberId, @Valid @RequestBody AccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createAccount(memberId, createRequest);
        return new ResultResponse<>(response);
    }


    // 적금 계좌 생성
    @PostMapping("/{memberId}/{savingProductId}")
    public ResultResponse<AccountCreateResponse> createSavingAccount(@PathVariable Long memberId,@PathVariable Long savingProductId,
                                                                     @Valid @RequestBody SavingAccountCreateRequest createRequest) {
        AccountCreateResponse response = bankFrontService.createSavingAccount(memberId, savingProductId, createRequest);
        return new ResultResponse<>(response);
    }

    // 입출금 거래내역
    @GetMapping("/checking-transaction-history/{accountId}")
    public ResultResponse<List<CheckingTransactionHistoryResponse>> getMyCheckTransactionHistory(Model model, @Authed Member member, @PathVariable long accountId) {
        List<CheckingTransactionHistoryResponse> response = bankFrontService.getCheckTransactionHistory(member.getId(), accountId);

        return new ResultResponse<>(response);
    }

    // 적금 거래내역
    @GetMapping("/saving-transaction-history/{accountId}")
    public ResultResponse<List<SavingTransactionHistoryResponse>> getMySaveTransactionHistory(Model model, @Authed Member member, @PathVariable long accountId) {
        List<SavingTransactionHistoryResponse> response = bankFrontService.getSaveTransactionHistory(member.getId(), accountId);

        return new ResultResponse<>(response);
    }

}
