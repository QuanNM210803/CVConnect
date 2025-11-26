package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum ProcessTypeEnum {
    APPLY("Ứng tuyển"),
    SCAN_CV("Lọc hồ sơ"),
    CONTEST("Thi tuyển"),
    INTERVIEW("Phỏng vấn"),
    OFFER("Đề nghị làm việc"),
    ONBOARD("Onboard");

    private final String name;

    ProcessTypeEnum(String name) {
        this.name = name;
    }
}
