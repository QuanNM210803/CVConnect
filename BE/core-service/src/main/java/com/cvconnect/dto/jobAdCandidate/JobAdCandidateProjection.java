package com.cvconnect.dto.jobAdCandidate;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.Instant;

public interface JobAdCandidateProjection extends BaseRepositoryDto {
    Long getJobAdId();
    String getJobAdTitle();
    Long getHrContactId();
    Long getPositionId();
    String getPositionName();
    Long getDepartmentId();
    String getDepartmentName();
    String getDepartmentCode();
    Long getJobAdCandidateId();
    String getCandidateStatus();
    Instant getApplyDate();
    Instant getOnboardDate();
    String getEliminateReasonType();
    String getEliminateReasonDetail();
    Instant getEliminateDate();
    Long getJobAdProcessCandidateId();
    Instant getActionDate();
    Boolean getIsCurrentProcess();
    Long getJobAdProcessId();
    String getProcessName();
    String getKeyCodeInternal();

    // candidate info
    Long getCandidateInfoId();
    String getFullName();
    String getEmail();
    String getPhone();
    Long getCandidateId();
    Long getLevelId();
    String getLevelName();
    String getTitle();
    Long getNumOfApply();
}
