package com.project.bankassetor.model.enums;

public enum AccountStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    EXPIRED("만기"),
    CLOSE("종료");

    private final String statusDescription;

    AccountStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
}
