package com.cvconnect.entity;

import com.cvconnect.enums.SendEmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.enums.EmailTemplateEnum;
import nmquan.commonlib.model.BaseEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "email_logs", schema = "cvconnect-notify-service")
public class EmailLog extends BaseEntity {
    @Size(max = 100)
    @Column(name = "email_group")
    private String emailGroup;

    @Size(max = 255)
    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipients", nullable = false)
    private String recipients;

    @Column(name = "cc_list")
    private String ccList;

    @Size(max = 255)
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "candidate_info_id")
    private Long candidateInfoId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "email_template_id")
    private Long emailTemplateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "template")
    private EmailTemplateEnum template;

    @Column(name = "template_variables")
    private String templateVariables;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private SendEmailStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "sent_at")
    private Instant sentAt;
}