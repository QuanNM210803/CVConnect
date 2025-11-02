package com.cvconnect.dto.candidateInfoApply;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.Instant;

public interface CandidateInfoApplyProjection extends BaseRepositoryDto {
    Long getCandidateId();
    String getFullName();
    String getEmail();
    String getPhone();
    Long getCvFileId();
    String getCoverLetter();
    Long getNumOfApply();

    String getCvFileUrl();
    String getSkill();
    Long getLevelId();
    String getLevelName();

    String getCandidateStatus();
    Instant getApplyDate();
    Instant getOnboardDate();

}
