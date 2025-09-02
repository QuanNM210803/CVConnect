package com.cvconnect.enums;

import com.cvconnect.entity.Candidate;
import com.cvconnect.entity.ManagementMember;
import com.cvconnect.entity.OrgMember;
import lombok.Getter;

@Getter
public enum MemberType {
    CANDIDATE("Ứng viên", Candidate.class),
    MANAGEMENT("Quản lý hệ thống", ManagementMember.class),
    ORGANIZATION("Thành viên của tổ chức", OrgMember.class);

    private final String name;
    private final Class<?> clazz;

    MemberType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}

