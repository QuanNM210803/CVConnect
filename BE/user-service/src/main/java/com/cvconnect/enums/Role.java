package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum Role {
    CANDIDATE("Ứng viên", MemberType.CANDIDATE),
    SYSTEM_ADMIN("Quản trị hệ thống", MemberType.MANAGEMENT),
    ORG_ADMIN("Quản trị viên tổ chức", MemberType.ORGANIZATION),
    HR("Quản trị nhân sự", MemberType.ORGANIZATION),
    INTERVIEWER("Người phỏng vấn", MemberType.ORGANIZATION),;

    private final String name;
    private final MemberType memberType;

    Role(String name, MemberType memberType) {
        this.name = name;
        this.memberType = memberType;
    }
}
