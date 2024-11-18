package com.project.bankassetor.primary.model.enums;

import lombok.Getter;

@Getter
public enum AccountStatus {
    active("활성"),
    inactive("비활성"),
    expired("만기"),
    close("종료");

    private final String statusDescription;

    AccountStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
