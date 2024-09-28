package com.project.bankassetor.service.front;

import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.BankAccount;
import com.project.bankassetor.model.request.AccountRequest;
import com.project.bankassetor.model.request.AccountTransferRequest;
import com.project.bankassetor.model.response.AccountResponse;
import com.project.bankassetor.model.response.AccountTransferResponse;
import com.project.bankassetor.service.perist.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankService bankService;


    // 입금
    public AccountResponse deposit(AccountRequest accountRequest) {

        final Account account = bankService.deposit(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 출금
    public AccountResponse withdraw(AccountRequest accountRequest) {

        final Account account = bankService.withdraw(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 계좌 이체
    public AccountTransferResponse transfer(AccountTransferRequest transferRequest) {

        final BankAccount transferAccount = bankService.transfer(transferRequest);

        // 응답 DTO 반환
        return new AccountTransferResponse(
                transferAccount.getUser().getName(),
                transferAccount.getAccount().getAccountNumber(),
                transferRequest.getAmount()
        );

    }
}
