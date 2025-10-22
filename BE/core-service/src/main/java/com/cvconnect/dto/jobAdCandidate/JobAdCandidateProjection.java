package com.cvconnect.dto.jobAdCandidate;

import java.time.Instant;

public interface JobAdCandidateProjection {
    Long getJobAdId();
    String getJobAdTitle();
    Long getHrContactId();
    Long getPositionId();
    String getPositionName();
    Long getDepartmentId();
    String getDepartmentName();
    Long getJobAdCandidateId();
    String getCandidateStatus();
    Instant getApplyDate();
    Long getJobAdProcessCandidateId();
    Instant getActionDate();
    Boolean getIsCurrentProcess();
    String getProcessName();
}
