package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum FailedRollbackType {
    UPDATE_ACCOUNT_STATUS("UPDATE_ACCOUNT_STATUS"),
    ;

    private final String type;
    FailedRollbackType(String type) {
        this.type = type;
    }
}
