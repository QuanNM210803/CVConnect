package com.cvconnect.dto.jobAd;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.Instant;

public interface JobAdProjection extends BaseRepositoryDto {

    String getCode();

    String getTitle();

    Long getOrgId();

    Long getPositionId();

    String getJobType();

    Instant getDueDate();

    Integer getQuantity();

    String getSalaryType();

    Integer getSalaryFrom();

    Integer getSalaryTo();

    String getCurrencyType();

    String getKeyword();

    String getDescription();

    String getRequirement();

    String getBenefit();

    Long getHrContactId();

    String getJobAdStatus();

    Boolean getIsPublic();

    Boolean getIsAutoSendEmail();

    Long getEmailTemplateId();

    Boolean getIsRemote();

    Boolean getIsAllLevel();

    String getKeyCodeInternal();

    Long getViewCount();
}