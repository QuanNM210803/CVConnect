package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum MemberType {
    CANDIDATE("Ứng viên", null),
    MANAGEMENT("Quản lý hệ thống", null),
    ORGANIZATION("Thành viên doanh nghiệp", null);

    private final String name;
    private final Class<?> clazz;

    MemberType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}

