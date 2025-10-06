package com.cvconnect.enums;

import com.cvconnect.dto.enums.JobAdStatusDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum JobAdStatus {
    DRAFT("Nháp", 0),
    OPEN("Đang tuyển", 1),
    PAUSE("Tạm dừng tuyển", 1),
    CLOSED("Đã đóng", 2);

    private final String description;
    private final int level;
    JobAdStatus(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public static JobAdStatus getJobAdStatus(String name) {
        for (JobAdStatus jobAdStatus : JobAdStatus.values()) {
            if (jobAdStatus.name().equalsIgnoreCase(name)) {
                return jobAdStatus;
            }
        }
        return null;
    }

    public static JobAdStatusDto getJobAdStatusDto(JobAdStatus jobAdStatus) {
        if (jobAdStatus == null) {
            return null;
        }
        return JobAdStatusDto.builder()
                .name(jobAdStatus.name())
                .description(jobAdStatus.getDescription())
                .level(jobAdStatus.getLevel())
                .build();
    }

    public static JobAdStatusDto getJobAdStatusDto(String name) {
        JobAdStatus jobAdStatus = getJobAdStatus(name);
        return getJobAdStatusDto(jobAdStatus);
    }

    public static List<JobAdStatusDto> getAll() {
        return Arrays.stream(JobAdStatus.values())
                .map(JobAdStatus::getJobAdStatusDto)
                .toList();
    }
}
