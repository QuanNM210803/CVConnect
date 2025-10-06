package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum CandidateStatus {
    APPLIED("Đã ứng tuyển"),
    VIEWED_CV("Đã xem hồ sơ"),
    IN_PROGRESS("Đang trong quá trình tuyển dụng"),
    WAITING_ONBOARDING("Chờ onboard"),
    ONBOARDED("Đã onboard"),
    NOT_ONBOARDED("Không onboard"),
    REJECTED("Hồ sơ bị từ chối");

    private final String label;

    CandidateStatus(String label) {
        this.label = label;
    }
}
