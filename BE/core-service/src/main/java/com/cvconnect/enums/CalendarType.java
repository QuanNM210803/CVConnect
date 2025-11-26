package com.cvconnect.enums;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.enums.CalendarTypeDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum CalendarType {
    INTERVIEW_OFFLINE("Phỏng vấn trực tiếp", Constants.OFFLINE),
    INTERVIEW_ONLINE("Phỏng vấn trực tuyến", Constants.ONLINE),
    TEST_OFFLINE("Thi tuyển trực tiếp", Constants.OFFLINE),
    TEST_ONLINE("Thi tuyển trực tuyến", Constants.ONLINE),
    PROBATION("Tiếp nhận thử việc", Constants.OFFLINE)

    ;
    private final String displayName;
    private final String type;
    CalendarType(String displayName, String type) {
        this.displayName = displayName;
        this.type = type;
    }

    public static CalendarType getCalendarType(String name) {
        for (CalendarType calendarType : CalendarType.values()) {
            if (calendarType.name().equalsIgnoreCase(name)) {
                return calendarType;
            }
        }
        return null;
    }

    public static CalendarTypeDto getCalendarTypeDto(CalendarType calendarType) {
        if (Objects.isNull(calendarType)) {
            return null;
        }
        return CalendarTypeDto.builder()
                .name(calendarType.name())
                .displayName(calendarType.getDisplayName())
                .type(calendarType.getType())
                .build();
    }

    public static CalendarTypeDto getCalendarTypeDto(String name) {
        CalendarType calendarType = getCalendarType(name);
        return getCalendarTypeDto(calendarType);
    }

    public static List<CalendarTypeDto> getAll() {
        return Arrays.stream(CalendarType.values())
                .map(CalendarType::getCalendarTypeDto)
                .toList();
    }

}
