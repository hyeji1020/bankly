package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.enums.AccountType;
import com.project.bankassetor.primary.repository.AccountRepository;
import com.project.bankassetor.primary.repository.SavingAccountRepository;
import com.project.bankassetor.primary.repository.SavingProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CheckingAccountService checkingAccountService;

    @Autowired
    private SavingAccountService savingAccountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SavingProductRepository savingProductRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

//    @BeforeEach
//    public void setUp() {
//        savingProductRepository.deleteAllInBatch();
//        savingAccountRepository.deleteAllInBatch();
//        accountRepository.deleteAllInBatch();
//    }

    @Test
    @DisplayName("입출금 계좌용 Account 및 CheckingAccount를 생성하고 저장한다")
    public void saveCheckingAccount() {
        List<Account> accounts = new ArrayList<>();
        List<CheckingAccount> chAccounts = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            String accountNumber = "1000000000" + i;
            BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1000, 100000));
            BigDecimal depositLimit = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(100, 10000));
            AccountStatus accountStatus = AccountStatus.valueOf("active");
            AccountType accountType = AccountType.valueOf("checking");

            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .depositLimit(depositLimit)
                    .status(accountStatus)
                    .type(accountType)
                    .build();

            long accountId = i;
            long memberId = ThreadLocalRandom.current().nextLong(1, 101);

            CheckingAccount checkingAccount = CheckingAccount.builder()
                    .accountId(accountId)
                    .memberId(memberId)
                    .build();

            accounts.add(account);
            chAccounts.add(checkingAccount);
        }
        accountService.saveAll(accounts);
        checkingAccountService.saveAll(chAccounts);

        long count = accountService.count();
        long chCount = checkingAccountService.count();
        Assertions.assertEquals(100, count, "100개의 계좌가 저장되지 않았습니다.");
        Assertions.assertEquals(100, chCount, "100개의 입출금 계좌가 저장되지 않았습니다.");
    }

    @Test
    @DisplayName("SavingAccount를 생성하고 저장한다")
    public void saveProductAndSavingAccount() {

        List<SavingProduct> savingProducts = savingProductRepository.findAll();
        List<SavingAccount> savingAccounts = new ArrayList<>();

        for (int i = 101; i <= 200; i++) {
            SavingProduct product = savingProducts.get((i - 101) % savingProducts.size());

            long memberId = ThreadLocalRandom.current().nextLong(1, 101);
            int currentDepositCount = (i % (product.getDurationInMonths() - 1)) + 1;

            long savingProductId = product.getId();
            BigDecimal monthlyDeposit = product.getSavingLimit();

            // SavingAccount 생성
            SavingAccount account = SavingAccount.builder()
                    .accountId((long) i)
                    .memberId(memberId)
                    .savingProductId(savingProductId)
                    .monthlyDeposit(monthlyDeposit)
                    .currentDepositCount(currentDepositCount)
                    .totalDepositCount(product.getDurationInMonths())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(product.getDurationInMonths()))
                    .build();

            savingAccounts.add(account);
        }
        savingAccountService.saveAll(savingAccounts);
        long SavingAccount = savingAccountService.count();
        Assertions.assertEquals(100, SavingAccount, "100개의 Saving Account가 저장되지 않았습니다.");
    }

    @Test
    @DisplayName("적금 계좌용 Account를 생성하고 저장한다")
    public void saveAccount() {

        List<SavingAccount> savingAccounts = savingAccountRepository.findAll();
        List<Account> accounts = new ArrayList<>();

        for (int i = 101; i <= 200; i++) {
            SavingAccount savingAccount = savingAccounts.get((i - 101) % savingAccounts.size());

            String accountNumber = "1000000000" + i;
            BigDecimal balance = BigDecimal.valueOf(savingAccount.getCurrentDepositCount())
                    .multiply(savingAccount.getMonthlyDeposit());

            BigDecimal depositLimit = BigDecimal.ZERO;

            if(savingAccount.getAccountId() == (long) i){
                depositLimit = savingAccount.getMonthlyDeposit();
            }

            AccountStatus status = AccountStatus.valueOf("active");
            AccountType type = AccountType.valueOf("saving");

            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .depositLimit(depositLimit)
                    .status(status)
                    .type(type)
                    .build();

            accounts.add(account);

        }

        accountService.saveAll(accounts);
        long count = accountService.count();
        Assertions.assertEquals(200, count, "100개의 Account 적금 계좌가 저장되지 않았습니다.");
    }

}
