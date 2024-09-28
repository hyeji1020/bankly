package com.project.bankassetor.api;

import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.model.response.AccountResponse;
import com.project.bankassetor.model.response.AccountTransferResponse;
import com.project.bankassetor.model.response.ResultResponse;
import com.project.bankassetor.service.front.BankFrontService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
