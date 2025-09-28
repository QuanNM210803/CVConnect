package com.cvconnect.enums;

import com.cvconnect.dto.jobAd.SalaryTypeDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum SalaryType {
    NEGOTIABLE("Thoả thuận"),
    RANGE(null);

    private final String description;
    SalaryType(String description) {
        this.description = description;
    }

    public static SalaryType getSalaryType(String name) {
        for (SalaryType salaryType : SalaryType.values()) {
            if (salaryType.name().equalsIgnoreCase(name)) {
                return salaryType;
            }
        }
        return null;
    }

    public static SalaryTypeDto getSalaryTypeDto(SalaryType salaryType) {
        if (Objects.isNull(salaryType)) {
            return null;
        }
        return SalaryTypeDto.builder()
                .name(salaryType.name())
                .description(salaryType.getDescription())
                .build();
    }

    public static SalaryTypeDto getSalaryTypeDto(String name) {
        SalaryType salaryType = getSalaryType(name);
        return getSalaryTypeDto(salaryType);
    }

    public static List<SalaryTypeDto> getAll() {
        return Arrays.stream(SalaryType.values())
                .map(SalaryType::getSalaryTypeDto)
                .toList();
    }
}
