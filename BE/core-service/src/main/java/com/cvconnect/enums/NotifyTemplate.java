package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import lombok.Getter;

@Getter
public enum NotifyTemplate {
    JOB_AD_CREATED(Messages.JOB_AD_CREATED_TITLE, Messages.JOB_AD_CREATED_MESSAGE),
    CANDIDATE_APPLY_JOB_AD(Messages.CANDIDATE_APPLY_JOB_AD_TITLE, Messages.CANDIDATE_APPLY_JOB_AD_MESSAGE)

    ;
    private final String title;
    private final String message;
    NotifyTemplate(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
