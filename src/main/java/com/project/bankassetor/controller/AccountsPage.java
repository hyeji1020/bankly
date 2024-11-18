package com.project.bankassetor.controller;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.request.InterestCalcRequest;
import com.project.bankassetor.primary.model.response.*;
import com.project.bankassetor.service.front.BankFrontService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountsPage {

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

    // 적금 상품 목록
    @GetMapping("/saving-products")
    public String getSavingProducts(Model model, @Authed Member member) {
        List<SavingProductResponse> savingProducts = bankFrontService.getSavingProducts();

        model.addAttribute("member", member);
        model.addAttribute("savingProducts", savingProducts);
        return "saving-products";
    }

    // 적금 상품
    @GetMapping("/saving-products-detail/{savingProductId}")
    public String getSavingProduct(@PathVariable Long savingProductId, Model model, @Authed Member member) {
        SavingProductResponse savingProduct = bankFrontService.getSavingProduct(savingProductId);

        model.addAttribute("member", member);
        model.addAttribute("savingProduct", savingProduct);
        model.addAttribute("id", savingProductId);
        return "saving-products-detail";
    }

    @GetMapping("/interest/calculate/{savingProductId}")
    public String interestCal(@PathVariable long savingProductId, Model model) {

        SavingProductResponse savingProduct = bankFrontService.getSavingProduct(savingProductId);

        model.addAttribute("savingProduct", savingProduct);
        model.addAttribute("savingProductId", savingProductId);
        return "interest-calculate";
    }

    @PostMapping("/interest/calculate/{savingProductId}")
    public String calculateProc(@PathVariable long savingProductId,
                                @ModelAttribute InterestCalcRequest interestCalcRequest, Model model) {

        SavingProductResponse savingProduct = bankFrontService.getSavingProduct(savingProductId);
        InterestCalcResponse response = bankFrontService.interestCalculate(savingProductId, interestCalcRequest);

        model.addAttribute("savingProduct", savingProduct);
        model.addAttribute("response", response);
        return "interest-calculate";
    }

}
