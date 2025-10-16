package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum NotifyTemplate {
    NEW_MEMBER_JOINED_ORG("New member joined organization", "A new member has joined your organization.")

    ;
    private final String title;
    private final String message;
    NotifyTemplate(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
