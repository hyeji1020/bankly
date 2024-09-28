package com.project.bankassetor.repository;

import com.project.bankassetor.model.entity.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class BankAccountRepository {
    Map<Long, BankAccount> accountMap = new HashMap<>();

    // 저장
    public BankAccount save(BankAccount bankAccount) {
        accountMap.put(bankAccount.getId(), bankAccount);
        return bankAccount;
    }

    // 수정
    public BankAccount update(Long id, BankAccount updatedAccount) {
        if (accountMap.containsKey(id)) {
            accountMap.put(id, updatedAccount);
            return updatedAccount;
        }
        return null;
    }

    // 계좌 찾기
    public BankAccount findBankAccountByAccountNumber(long accountNumber) {
        return accountMap.values().stream()
                .filter(bankAccount -> bankAccount.getAccount().getAccountNumber() == accountNumber)
                .findFirst()
                .orElse(null);
    }

    // 계좌 아이디로 계좌 찾기
    public BankAccount findBankAccountByAccountId(long accountId) {
        return accountMap.values().stream()
                .filter(bankAccount -> bankAccount.getAccount().getId() == accountId)
                .findFirst()
                .orElse(null);
    }

    // 전체 계좌 개수 반환
    public int getTotalBankAccounts() {
        return accountMap.size();
    }

    // 저장된 계좌 정보 출력
    public void printAccounts() {
        for (Map.Entry<Long, BankAccount> entry : accountMap.entrySet()) {
            BankAccount bankAccount = entry.getValue();
            log.info("아이디: {}, 계좌번호: {}, 사용자: {}, 잔액: {}",
                    bankAccount.getId(),
                    bankAccount.getAccount().getAccountNumber(),
                    bankAccount.getUser().getName(),
                    bankAccount.getAccount().getBalance());
        }
    }

    // 데이터 초기화
    public void deleteAll() {
        accountMap.clear();
    }
}
