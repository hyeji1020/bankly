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
import org.springframework.http.HttpStatus;
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
    public ResultResponse<List<TransactionHistoryResponse>> findBalanceHistory(@PathVariable Long accountId) {
        List<TransactionHistoryResponse> response = bankFrontService.findBalanceHistory(accountId);
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

    // 나의 당좌 계좌 목록
    @GetMapping("/my-checking-accounts")
    public ResultResponse<List<AccountResponse>> getMyCheckAccounts(@Authed Member member) {
        if (member == null) {
            return new ResultResponse<>(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        long memberId = member.getId();

        List<AccountResponse> response = bankFrontService.getMyCheckAccounts(memberId);
        return new ResultResponse<>(response);
    }

    // 나의 적금 계좌 목록
    @GetMapping("/my-saving-accounts")
    public ResultResponse<List<AccountResponse>> getMySaveAccounts(@Authed Member member) {
        if (member == null) {
            return new ResultResponse<>(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        long memberId = member.getId();

        List<AccountResponse> response = bankFrontService.getMySaveAccounts(memberId);
        return new ResultResponse<>(response);
    }

}
