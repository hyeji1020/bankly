package com.project.bankassetor.controller;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.response.AccountResponse;
import com.project.bankassetor.primary.model.response.CheckingTransactionHistoryResponse;
import com.project.bankassetor.primary.model.response.SavingTransactionHistoryResponse;
import com.project.bankassetor.service.front.BankFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyAccountsPage {

    private final BankFrontService bankFrontService;

    // 나의 모든 계좌 목록
    @GetMapping("/my-accounts")
    public String getMyAccounts(Model model, @Authed Member member) {
        List<AccountResponse> checkAccounts = bankFrontService.getMyCheckAccounts(member.getId());
        List<AccountResponse> savingAccounts = bankFrontService.getMySaveAccounts(member.getId());

        model.addAttribute("member", member);
        model.addAttribute("myCheckAccounts", checkAccounts);
        model.addAttribute("mySaveAccounts", savingAccounts);
        return "my-accounts";
    }

    // 입출금 거래내역
    @GetMapping("/checking-transaction-history/{accountId}")
    public String getMyCheckTransactionHistory(Model model, @Authed Member member, @PathVariable long accountId) {
        List<CheckingTransactionHistoryResponse> checkHistory = bankFrontService.getCheckTransactionHistory(accountId);

        model.addAttribute("member", member);
        model.addAttribute("myCheckHistory", checkHistory);
        model.addAttribute("type", "checking");
        return "transaction-history";
    }

    // 적금 거래내역
    @GetMapping("/saving-transaction-history/{accountId}")
    public String getMySaveTransactionHistory(Model model, @Authed Member member, @PathVariable long accountId) {
        List<SavingTransactionHistoryResponse> saveHistory = bankFrontService.getSaveTransactionHistory(accountId);

        model.addAttribute("member", member);
        model.addAttribute("mySaveHistory", saveHistory);
        model.addAttribute("type", "saving");
        return "transaction-history";
    }

}
