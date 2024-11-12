package com.project.bankassetor.primary.model.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    TRANSFER("계좌이체"),
    PAYMENT("결제"),
    INTEREST_CREDIT("이자 지급"),
    FEE("수수료"),
    PURCHASE("구매"),
    CASH_BACK("캐시백");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

}
