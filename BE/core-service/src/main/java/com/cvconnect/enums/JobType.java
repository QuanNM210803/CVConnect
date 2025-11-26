package com.cvconnect.enums;

import com.cvconnect.dto.enums.JobTypeDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum JobType {
    FULL_TIME("Toàn thời gian"),
    PART_TIME("Bán thời gian"),
    CONTRACT("Hợp đồng"),
    ;

    private final String description;
    JobType(String description) {
        this.description = description;
    }

    public static JobType getJobType(String name) {
        for (JobType jobType : JobType.values()) {
            if (jobType.name().equalsIgnoreCase(name)) {
                return jobType;
            }
        }
        return null;
    }

    public static JobTypeDto getJobTypeDto(JobType jobType) {
        if (Objects.isNull(jobType)) {
            return null;
        }
        return JobTypeDto.builder()
                .name(jobType.name())
                .description(jobType.getDescription())
                .build();
    }

    public static JobTypeDto getJobTypeDto(String name) {
        JobType jobType = getJobType(name);
        return getJobTypeDto(jobType);
    }

    public static List<JobTypeDto> getAll() {
        return Arrays.stream(JobType.values())
                .map(JobType::getJobTypeDto)
                .toList();
    }
}
