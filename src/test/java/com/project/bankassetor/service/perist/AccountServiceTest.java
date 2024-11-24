package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.Account;
import com.project.bankassetor.primary.model.entity.account.check.CheckingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingAccount;
import com.project.bankassetor.primary.model.entity.account.save.SavingProduct;
import com.project.bankassetor.primary.model.enums.AccountStatus;
import com.project.bankassetor.primary.model.enums.AccountType;
import com.project.bankassetor.primary.repository.AccountRepository;
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

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
        savingProductRepository.deleteAll();
    }

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
                    .accountStatus(accountStatus)
                    .accountType(accountType)
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

        long count = accountService.count(); // 데이터 개수 확인 메서드
        long chCount = checkingAccountService.count(); // 데이터 개수 확인 메서드
        Assertions.assertEquals(100, count, "100개의 계좌가 저장되지 않았습니다.");
        Assertions.assertEquals(100, chCount, "100개의 입출금 계좌가 저장되지 않았습니다.");
    }

    @Test
    @DisplayName("적금 계좌용 Account 및 SavingAccount를 생성하고 저장한다")
    public void saveAccount() {
        List<Account> accounts = new ArrayList<>();
        for(int i = 101; i <= 200; i++){
            String accountNumber = "1000000000" + i;
            BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1000, 100000));
            BigDecimal depositLimit = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(100, 10000));
            AccountStatus accountStatus = AccountStatus.valueOf("active");
            AccountType accountType = AccountType.valueOf("saving");

            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .depositLimit(depositLimit)
                    .accountStatus(accountStatus)
                    .accountType(accountType)
                    .build();

            accounts.add(account);

        }
        accountService.saveAll(accounts);

        List<SavingProduct> savingProducts = savingProductRepository.findAll();
        List<SavingAccount> savingAccounts  = new ArrayList<>();
        for(int i = 101; i <= 200; i++) {
            SavingProduct product = savingProducts.get((i - 101) % savingProducts.size());

            long memberId = ThreadLocalRandom.current().nextLong(1, 101);

            // SavingAccount 생성
            SavingAccount account = SavingAccount.builder()
                    .accountId((long) i)
                    .memberId(memberId)
                    .savingProductId((long) (i % 10 + 1)) // (1~10 중 하나)
                    .monthlyDeposit(BigDecimal.valueOf(1000 * (i - 100))) // 1000 * (i - 100)
                    .currentDepositCount(0)
                    .totalDepositCount(product.getDurationInMonths())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(product.getDurationInMonths()))
                    .build();

            savingAccounts.add(account);
        }

        savingAccountService.saveAll(savingAccounts);

        long count = accountService.count();
        long SavingAccount = savingAccountService.count();

        Assertions.assertEquals(200, count, "100개의 Account 적금 계좌가 저장되지 않았습니다.");
        Assertions.assertEquals(100, SavingAccount, "100개의 Saving Account가 저장되지 않았습니다.");
    }

}
