package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum RoomSocketType {
    GROUP("GROUP", "group:"),
    USER("USER", "user:")

    ;

    private final String value;
    private final String prefix;
    RoomSocketType(String value, String prefix) {
        this.value = value;
        this.prefix = prefix;
    }
}
