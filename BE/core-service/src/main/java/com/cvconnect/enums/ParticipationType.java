package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum ParticipationType {
    CREATED_BY_ME("Tôi tạo"),
    JOINED_BY_ME("Tôi tham gia");

    private final String label;
    ParticipationType(String label) {
        this.label = label;
    }

}
