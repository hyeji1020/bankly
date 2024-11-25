package com.project.bankassetor.primary.model.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    deposit("입금"),
    withdraw("출금"),
    transfer("계좌이체"),
    payment("결제"),
    interestCredit("이자 지급"),
    fee("수수료"),
    purchase("구매"),
    cashBack("캐시백"),
    termination("중도해지");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

}
