package com.cvconnect.dto.jobAd;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.Instant;

public interface JobAdOrgFilterProjection extends BaseRepositoryDto {
    String getCode();
    String getTitle();
    Long getPositionId();
    String getPositionName();
    Long getDepartmentId();
    String getDepartmentName();
    Instant getDueDate();
    Integer getQuantity();
    Long getHrContactId();
    String getJobAdStatus();
    Boolean getIsPublic();
    String getKeyCodeInternal();
}
