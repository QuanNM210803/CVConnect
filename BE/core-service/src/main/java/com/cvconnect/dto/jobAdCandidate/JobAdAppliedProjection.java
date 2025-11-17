package com.cvconnect.dto.jobAdCandidate;

import java.time.Instant;

public interface JobAdAppliedProjection {

    Long getJobAdId();
    String getJobAdTitle();
    Long getHrContactId();

    Long getJobAdCandidateId();
    String getCandidateStatus();
    Instant getApplyDate();
    Instant getOnboardDate();
    String getEliminateReasonType();
    Instant getEliminateDate();

    Long getJobAdProcessId();
    String getProcessName();
    Instant getTransferDate();

    Long getOrgId();
    String getOrgName();
    Long getLogoId();

    String getFullName();
    String getPhone();
    String getEmail();
    String getCoverLetter();
    Long getCvFileId();
}
