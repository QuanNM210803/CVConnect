package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import lombok.Getter;

@Getter
public enum NotifyTemplate {
    NEW_MEMBER_JOINED_ORG(Messages.NEW_MEMBER_JOINED_ORG_TITLE, Messages.NEW_MEMBER_JOINED_ORG_MESSAGE)

    ;
    private final String title;
    private final String message;
    NotifyTemplate(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
