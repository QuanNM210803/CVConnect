package com.cvconnect.enums;

import com.cvconnect.dto.enums.CandidateStatusDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum CandidateStatus {
    APPLIED("Đã ứng tuyển"),
    VIEWED_CV("Đã xem hồ sơ"),
    IN_PROGRESS("Đang xử lý"),
    WAITING_ONBOARDING("Chờ onboard"),
    ONBOARDED("Đã onboard"),
    NOT_ONBOARDED("Không onboard"),
    REJECTED("Từ chối");

    private final String label;

    CandidateStatus(String label) {
        this.label = label;
    }

    public static CandidateStatus getCandidateStatus(String name) {
        for (CandidateStatus candidateStatus : CandidateStatus.values()) {
            if (candidateStatus.name().equalsIgnoreCase(name)) {
                return candidateStatus;
            }
        }
        return null;
    }

    public static CandidateStatusDto getCandidateStatusDto(CandidateStatus candidateStatus) {
        if (Objects.isNull(candidateStatus)) {
            return null;
        }
        return CandidateStatusDto.builder()
                .label(candidateStatus.getLabel())
                .build();
    }

    public static CandidateStatusDto getCandidateStatusDto(String name) {
        CandidateStatus candidateStatus = getCandidateStatus(name);
        return getCandidateStatusDto(candidateStatus);
    }

    public static List<CandidateStatusDto> getAll() {
        return Arrays.stream(CandidateStatus.values())
                .map(CandidateStatus::getCandidateStatusDto)
                .toList();
    }
}
