package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum SendEmailStatus {
    SENDING("Đang gửi"),
    SUCCESS("Thành công"),
    TEMPORARY_FAILURE("Thất bại tạm thời"),
    FAILURE_WAIT_RESEND("Thất bại, chờ gửi lại"),
    FAILURE("Thất bại");

    private final String name;
    SendEmailStatus(String name) {
        this.name = name;
    }
}
