package com.cvconnect.dto.candidateEvaluation;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.math.BigDecimal;

public interface CandidateEvaluationProjection extends BaseRepositoryDto {
    Long getJobAdProcessCandidateId();
    Long getEvaluatorId();
    String getComments();
    BigDecimal getScore();
    Long getJobAdProcessId();
    String getJobAdProcessName();
}
