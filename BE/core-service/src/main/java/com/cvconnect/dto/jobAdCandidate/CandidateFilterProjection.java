package com.cvconnect.dto.jobAdCandidate;

import java.time.Instant;

public interface CandidateFilterProjection {
    Long getCandidateInfoId();
    Long getJobAdId();
    String getJobAdTitle();
    String getCandidateStatus();
    Long getProcessTypeId();
    String getProcessTypeName();
    String getProcessTypeCode();
    Instant getApplyDate();
    Long getHrContactId();
}
