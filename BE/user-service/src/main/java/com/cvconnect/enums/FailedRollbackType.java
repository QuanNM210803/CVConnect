package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum FailedRollbackType {
    ORG_CREATION("ORG_CREATION"),
    ;

    private final String type;
    FailedRollbackType(String type) {
        this.type = type;
    }
}
