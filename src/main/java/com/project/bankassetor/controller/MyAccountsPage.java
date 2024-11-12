package com.project.bankassetor.controller;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.response.AccountResponse;
import com.project.bankassetor.service.front.BankFrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyAccountsPage {

    private final BankFrontService bankFrontService;

    // 나의 모든 계좌 목록
    @GetMapping("/my-accounts")
    public String getMyCheckAccounts(Model model, @Authed Member member) {
        List<AccountResponse> check = bankFrontService.getMyCheckAccounts(member.getId());
        List<AccountResponse> saving = bankFrontService.getMySaveAccounts(member.getId());

        model.addAttribute("member", member);
        model.addAttribute("myCheckAccounts", check);
        model.addAttribute("mySaveAccounts", saving);
        return "my-accounts";
    }

    // 나의 모든 계좌 목록
    @GetMapping("/history-transaction")
    public String getMyHistoryTransaction(Model model, @Authed Member member) {
        List<AccountResponse> check = bankFrontService.getMyCheckAccounts(member.getId());
        List<AccountResponse> saving = bankFrontService.getMySaveAccounts(member.getId());

        model.addAttribute("member", member);
        model.addAttribute("myCheckAccounts", check);
        model.addAttribute("mySaveAccounts", saving);
        return "my-accounts";
    }

}
