package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "email_template_placeholder", schema = "cvconnect-notify-service")
public class EmailTemplatePlaceholder extends BaseEntity {
    @NotNull
    @Column(name = "email_template_id", nullable = false)
    private Long emailTemplateId;

    @NotNull
    @Column(name = "placeholder_id", nullable = false)
    private Long placeholderId;

}