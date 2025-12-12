package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import lombok.Getter;

@Getter
public enum NotifyTemplate {
    NEW_MEMBER_JOINED_ORG(Messages.NEW_MEMBER_JOINED_ORG_TITLE, Messages.NEW_MEMBER_JOINED_ORG_MESSAGE),
    NEW_ORGANIZATION_CREATED(Messages.NEW_ORGANIZATION_CREATED_TITLE, Messages.NEW_ORGANIZATION_CREATED_MESSAGE),
    REJECTED_INVITE_JOIN_ORG(Messages.REJECTED_INVITE_JOIN_ORG_TITLE, Messages.REJECTED_INVITE_JOIN_ORG_MESSAGE),
    ACTIVE_ORG_MEMBER(Messages.ACTIVE_ORG_MEMBER_TITLE, Messages.ACTIVE_ORG_MEMBER_MESSAGE),
    DEACTIVE_ORG_MEMBER(Messages.DEACTIVE_ORG_MEMBER_TITLE, Messages.DEACTIVE_ORG_MEMBER_MESSAGE)

    ;
    private final String title;
    private final String message;
    NotifyTemplate(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
