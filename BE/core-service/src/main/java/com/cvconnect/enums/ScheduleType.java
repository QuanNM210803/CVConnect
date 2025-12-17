package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum ScheduleType {
    CRON("Cron"),
    FIXED_RATE("Fixed Rate"),
    FIXED_DELAY("Fixed Delay")
    ;

    private final String name;
    ScheduleType(String name) {
        this.name = name;
    }
}
