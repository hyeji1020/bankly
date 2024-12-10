package com.project.bankassetor.controller;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.enums.AccountType;
import com.project.bankassetor.primary.model.request.AccountRequest;
import com.project.bankassetor.primary.model.request.InterestCalcRequest;
import com.project.bankassetor.primary.model.request.SavingAccountCreateRequest;
import com.project.bankassetor.primary.model.request.StringMultiValueMapAdapter;
import com.project.bankassetor.primary.model.response.*;
import com.project.bankassetor.service.front.BankFrontService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountsPage {

    private final BankFrontService bankFrontService;

    // 나의 계좌 목록
    @GetMapping("/my-accounts")
    public String getMyAccounts(Model model, @Authed Member member) {

        List<AccountType> accountTypes = List.of(AccountType.checking, AccountType.saving);

        model.addAttribute("member", member);
        model.addAttribute("accountTypes", accountTypes);
        return "my-accounts";
    }

    // 나의 모든 계좌 목록(ajax)
    @PostMapping("/my-accounts")
    @ResponseBody
    public DataTableView getMyAccountsData(@Authed Member member, @RequestParam(required = false) MultiValueMap<String, String> param) {

        return bankFrontService.getMyAllAccounts(member, new StringMultiValueMapAdapter(param));
    }

//    // 나의 모든 계좌 목록
//    @GetMapping("/my-account/{accountId}")
//    public String getMyAccountsData(Model model, @PathVariable long accountId, @Authed Member member) {
//
//        AccountResponse account  = bankFrontService.getAccount(accountId);
//
//        model.addAttribute("account", account);
//
//        return "my-accounts";
//    }

    // 적금 거래내역 목록(ajax)
    @PostMapping("/checking-transaction-history/{accountId}")
    @ResponseBody
    public DataTableView getMyCheckTransactionHistory(@PathVariable long accountId, @Authed Member member, @RequestParam(required = false) MultiValueMap<String, String> param) {

        return bankFrontService.getAllCheckingTx(accountId, member, new StringMultiValueMapAdapter(param));
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

    // 적금 거래내역 목록(ajax)
    @PostMapping("/saving-transaction-history/{accountId}")
    @ResponseBody
    public DataTableView getMySaveTransactionHistory(@PathVariable long accountId, @Authed Member member, @RequestParam(required = false) MultiValueMap<String, String> param) {

        return bankFrontService.getAllSavingTx(accountId, member, new StringMultiValueMapAdapter(param));
    }

    // 적금 거래내역 목록
    @GetMapping("/saving-transaction-history/{accountId}")
    public String getMySaveTransactionHistory(Model model, @Authed Member member, @PathVariable long accountId) {
        List<SavingTransactionHistoryResponse> saveHistory = bankFrontService.getSaveTransactionHistory(accountId);

        model.addAttribute("member", member);
        model.addAttribute("mySaveHistory", saveHistory);
        model.addAttribute("type", "saving");
        model.addAttribute("accountId", accountId);
        return "transaction-history";
    }

    // 적금 상품 목록
    @GetMapping("/saving-products")
    public String getSavingProducts(Model model, @Authed Member member) {

        model.addAttribute("member", member);
        return "saving-products";
    }

    // 적금 상품 목록(ajax)
    @PostMapping("/saving-products")
    @ResponseBody
    public DataTableView getSavingProducts(@Authed Member member, @RequestParam(required = false) MultiValueMap<String, String> param) {

        return bankFrontService.getAllSavingProducts(member, new StringMultiValueMapAdapter(param));
    }

    // 적금 상품 상세
    @GetMapping("/saving-products-detail/{savingProductId}")
    public String getSavingProduct(@PathVariable Long savingProductId, Model model, @Authed Member member) {
        SavingProductResponse savingProduct = bankFrontService.getSavingProduct(savingProductId);

        model.addAttribute("member", member);
        model.addAttribute("savingProduct", savingProduct);
        model.addAttribute("id", savingProductId);
        return "saving-products-detail";
    }

    // 적금 상품 이자 계산기 뷰
    @GetMapping("/interest/calculate/{savingProductId}")
    public String interestCal(@PathVariable long savingProductId, Model model, @Authed Member member) {

        SavingProductResponse savingProduct = bankFrontService.getSavingProduct(savingProductId);

        model.addAttribute("member", member);
        model.addAttribute("savingProduct", savingProduct);
        model.addAttribute("savingProductId", savingProductId);
        return "interest-calculate";
    }

    // 적금 상품 이자 계산기
    @PostMapping("/interest/calculate/{savingProductId}")
    @ResponseBody
    public InterestCalcResponse calculateInterest(@PathVariable long savingProductId,
                                                  @Valid @RequestBody InterestCalcRequest request, Model model) {

        return bankFrontService.interestCalculate(savingProductId, request);
    }

    // 계좌 중도 해지 뷰
    @GetMapping("/my-saving-accounts/{accountId}/terminate")
    public String calculateProc(@PathVariable long accountId, @Authed Member member, Model model) {

        InterestCalcResponse terminateAmount = bankFrontService.expectInterest(accountId);

        model.addAttribute("terminateAmount", terminateAmount);
        model.addAttribute("member", member);
        model.getAttribute("accountId");
        return "expect-interest";
    }

    // 계좌 중도 해지
    @PostMapping("/my-saving-accounts/{accountId}/terminate")
    public String maturityProc(@PathVariable long accountId, @Authed Member member, Model model) {

        SavingTransactionHistoryResponse savingTxResponse = bankFrontService.terminateSavingAccount(accountId, member.getId());

        model.addAttribute("savingTxResponse", savingTxResponse);
        model.addAttribute("member", member);
        model.getAttribute("accountId");
        return "redirect:/my-accounts";
    }

    // 계좌 생성하기 뷰
    @GetMapping("/saving-products/{savingProductId}/accounts")
    public String createSavingAccount(@Authed Member member, Model model, @PathVariable long savingProductId){
        model.addAttribute("member", member);
        model.addAttribute("savingProductId", savingProductId);
        model.getAttribute("savingProduct");
        return "create-account";
    }

    // 계좌 생성하기
    @PostMapping("/saving-products/{savingProductId}/accounts")
    public String createSavingAccount(@PathVariable long savingProductId, @Authed Member member, Model model,
                                      @Valid @ModelAttribute SavingAccountCreateRequest request) {

        AccountCreateResponse response = bankFrontService.createSavingAccount(member.getId(), savingProductId, request);

        model.addAttribute("member", member);
        model.addAttribute("response", response);
        return "redirect:/my-accounts";
    }

    // 계좌 이체 페이지
    @GetMapping("/{accountId}/transfer")
    public String transferPage(@PathVariable long accountId, @Authed Member member, Model model) {

        model.addAttribute("member", member);
        model.addAttribute("accountId", accountId);

        return "transfer";
    }

    // 계좌 이체하기
    @PutMapping("/{accountId}/transferProc")
    @ResponseBody
    public AccountTransferResponse transferProc(@PathVariable long accountId,
                               @Valid @RequestBody AccountRequest request) {

        return bankFrontService.transfer(accountId, request);
    }

}
