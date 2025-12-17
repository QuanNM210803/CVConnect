package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum FailedRollbackType {
    ORG_CREATION("ORG_CREATION"),
    UPLOAD_FILE("UPLOAD_FILE")
    ;

    private final String type;
    FailedRollbackType(String type) {
        this.type = type;
    }
}
