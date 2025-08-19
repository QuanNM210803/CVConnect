package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum MemberType {
    CANDIDATE("Ứng viên"),
    MANAGEMENT("Thành viên quản lý"),
    ORGANIZATION("Thành viên tổ chức");

    private final String name;

    MemberType(String name) {
        this.name = name;
    }
}

