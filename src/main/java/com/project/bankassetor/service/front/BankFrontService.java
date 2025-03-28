package com.project.bankassetor.service.front;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.entity.account.check.CheckingTransactionHistory;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.entity.account.save.SavingTransactionHistory;
import com.project.bankassetor.primary.model.request.*;
import com.project.bankassetor.primary.model.response.*;
import com.project.bankassetor.service.perist.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final CheckingTransactionHistoryService historyService;
    private final AccountService accountService;
    private final CheckingTransactionHistoryService checkingTransactionHistoryService;
    private final SavingTransactionHistoryService savingTransactionHistoryService;
    private final SavingProductService savingProductService;
    private final SavingAccountService savingAccountService;

    // 입금
    public AccountResponse deposit(AccountRequest accountRequest) {

        final Account account = accountService.deposit(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 출금
    public AccountResponse withdraw(AccountRequest accountRequest) {

        final Account account = accountService.withdraw(accountRequest);

        // 응답 DTO 반환
        return AccountResponse.of(account);

    }

    // 계좌 이체
    public AccountTransferResponse transfer(Long fromAccountId, AccountRequest accountRequest) {

        final Account transferAccount = accountService.transfer(fromAccountId, accountRequest);

        // 응답 DTO 반환
        return AccountTransferResponse.of(transferAccount, accountRequest.getAmount());

    }

    // 계좌 생성
    public AccountCreateResponse createAccount(Long memberId, AccountCreateRequest createRequest) {

        final Account account = accountService.createAccount(memberId, createRequest);

        return AccountCreateResponse.of(account);
    }

    public AccountCreateResponse createSavingAccount(Long memberId, Long savingProductId, SavingAccountCreateRequest createRequest) {

        final Account account = accountService.createSavingAccount(memberId, savingProductId, createRequest);

        return AccountCreateResponse.of(account);
    }

    // 나의 당좌 계좌 목록 확인
    public List<AccountResponse> getMyCheckAccounts(Long memberId) {
        final List<Account> accounts = accountService.findCheckByMemberId(memberId);

        return AccountResponse.of(accounts);
    }

    // 나의 적금 계좌 목록 확인
    public List<AccountResponse> getMySaveAccounts(Long memberId) {
        final List<Account> accounts = accountService.findSaveByMemberId(memberId);

        return AccountResponse.of(accounts);
    }

    public List<CheckingTransactionHistoryResponse> getCheckTransactionHistory(Long accountId) {
        final List<CheckingTransactionHistory> checkHistories = checkingTransactionHistoryService.findCheckTransactionHistoryByAccountId(accountId);

        return CheckingTransactionHistoryResponse.of(checkHistories);
    }

    public List<SavingTransactionHistoryResponse> getSaveTransactionHistory(Long accountId) {
        final List<SavingTransactionHistory> saveHistories = savingTransactionHistoryService.findSaveTransactionHistoryByAccountId(accountId);

        return SavingTransactionHistoryResponse.of(saveHistories);
    }

    public List<SavingProductResponse> getSavingProducts() {
        final List<SavingProduct> savingProducts = savingProductService.findAll();

        return SavingProductResponse.of(savingProducts);
    }

    public SavingProductResponse getSavingProduct(Long savingProductId) {
        final SavingProduct savingProduct = savingProductService.findById(savingProductId);

        return SavingProductResponse.of(savingProduct);
    }

    public InterestCalcResponse interestCalculate(Long savingProductId, InterestCalcRequest interestCalcRequest) {
        return savingProductService.calculateInterest(savingProductId, interestCalcRequest);

    }

    public InterestCalcResponse expectInterest(Long accountId) {
        return savingAccountService.expectInterest(accountId);

    }

    public SavingTransactionHistoryResponse terminateSavingAccount(Long accountId, Long memberId) {
        final SavingTransactionHistory saveHistory = savingAccountService.terminateSavingAccount(accountId, memberId);

        return SavingTransactionHistoryResponse.of(saveHistory);
    }

    // 나의 계좌 목록(데이터 테이블)
    public DataTableView getMyAllAccounts(Member member, StringMultiValueMapAdapter param) {

        int draw = param.intVal("draw");
        int start = param.intVal("start");
        int length = param.intVal("length");
        String accountType = param.stringVal("accountType", null);
        String keyword = param.stringVal("keyword", null);

        int pageNumber = (start / length);
        final PageRequest pageable = PageRequest.of(pageNumber, length);

        final Page<Account> accountPage = accountService.findAllByMemberId(accountType, keyword, member.getId(), pageable);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("accounts", accountPage);

        return new DataTableView(draw, accountPage.getTotalElements(), accountPage.getTotalElements(), data);
    }

    // 적금 상품 목록(데이터 테이블)
    public DataTableView getAllSavingProducts(Member member, StringMultiValueMapAdapter param) {

        int draw = param.intVal("draw");
        int start = param.intVal("start");
        int length = param.intVal("length");
        String keyword = param.stringVal("keyword", null);

        int pageNumber = (start / length);
        final PageRequest pageable = PageRequest.of(pageNumber, length);

        final Page<SavingProduct> savingProductPage = savingProductService.getAllSavingProducts(keyword, pageable);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("savingProducts", savingProductPage);

        return new DataTableView(draw, savingProductPage.getTotalElements(), savingProductPage.getTotalElements(), data);

    }

    // 적금 거래 내역 목록(데이터 테이블)
    public DataTableView getAllCheckingTx(Long accountId, Member member, StringMultiValueMapAdapter param) {

        int draw = param.intVal("draw");
        int start = param.intVal("start");
        int length = param.intVal("length");
        String txType = param.stringVal("txType", null);

        int pageNumber = (start / length);
        final PageRequest pageable = PageRequest.of(pageNumber, length);

        final Page<CheckingTransactionHistory> checkingTxPage = historyService.findAllByAccountIdAndType(accountId, txType, member.getId(), pageable);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("checkingTx", checkingTxPage);

        return new DataTableView(draw, checkingTxPage.getTotalElements(), checkingTxPage.getTotalElements(), data);
    }

    // 적금 거래 내역 목록(데이터 테이블)
    public DataTableView getAllSavingTx(Long accountId, Member member, StringMultiValueMapAdapter param) {

        int draw = param.intVal("draw");
        int start = param.intVal("start");
        int length = param.intVal("length");
        String txType = param.stringVal("txType", null);

        int pageNumber = (start / length);
        final PageRequest pageable = PageRequest.of(pageNumber, length);
;
        final Page<SavingTransactionHistory> savingTxPage = savingTransactionHistoryService.findAllByAccountIdAndType(accountId, txType, member.getId(), pageable);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("savingTx", savingTxPage);

        return new DataTableView(draw, savingTxPage.getTotalElements(), savingTxPage.getTotalElements(), data);
    }
}
