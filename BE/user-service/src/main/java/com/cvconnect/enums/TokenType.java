package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS,
    REFRESH,
    VERIFY_EMAIL,
    RESET_PASSWORD
}
