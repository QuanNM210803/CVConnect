package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum MemberType {
    CANDIDATE("Ứng viên"),
    MANAGEMENT("Quản lý hệ thống"),
    ORGANIZATION("Thành viên của tổ chức");

    private final String name;

    MemberType(String name) {
        this.name = name;
    }
}

