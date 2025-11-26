package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_ad")
public class JobAd extends BaseEntity {
    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @NotNull
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Size(max = 100)
    @NotNull
    @Column(name = "job_type", nullable = false, length = 100)
    private String jobType;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @ColumnDefault("1")
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 100)
    @NotNull
    @Column(name = "salary_type", nullable = false, length = 100)
    private String salaryType;

    @Column(name = "salary_from")
    private Integer salaryFrom;

    @Column(name = "salary_to")
    private Integer salaryTo;

    @Size(max = 50)
    @NotNull
    @Column(name = "currency_type", nullable = false, length = 50)
    private String currencyType;

    @Size(max = 255)
    @Column(name = "keyword")
    private String keyword;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "requirement", length = Integer.MAX_VALUE)
    private String requirement;

    @Column(name = "benefit", length = Integer.MAX_VALUE)
    private String benefit;

    @NotNull
    @Column(name = "hr_contact_id", nullable = false)
    private Long hrContactId;

    @Size(max = 100)
    @NotNull
    @Column(name = "job_ad_status", nullable = false, length = 100)
    private String jobAdStatus;

    @ColumnDefault("true")
    @Column(name = "is_public")
    private Boolean isPublic;

    @ColumnDefault("false")
    @Column(name = "is_auto_send_email")
    private Boolean isAutoSendEmail;

    @Column(name = "email_template_id")
    private Long emailTemplateId;

    @Column(name = "is_remote")
    private Boolean isRemote;

    @Column(name = "is_all_level")
    private Boolean isAllLevel;

    @Column(name = "key_code_internal")
    private String keyCodeInternal;
}