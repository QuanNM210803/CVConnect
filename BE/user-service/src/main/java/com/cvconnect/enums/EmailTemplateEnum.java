package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateEnum {
    VERIFY_EMAIL("verify_email", "Verify your email"),
    RESET_PASSWORD("reset_password", "Reset your password"),
    VERIFY_ORG_EMAIL("verify_org_email", "Verify your organization email"),
    VERIFY_MEMBER_ORG_EMAIL("verify_member_org_email", "Verify your member organization email")
    ;

    private final String templateName;
    private final String subject;

    EmailTemplateEnum(String templateName, String subject) {
        this.templateName = templateName;
        this.subject = subject;
    }
}
