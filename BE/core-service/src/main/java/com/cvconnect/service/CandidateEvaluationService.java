package com.cvconnect.service;

import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationDetail;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationRequest;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface CandidateEvaluationService {
    IDResponse<Long> create(CandidateEvaluationRequest request);
    IDResponse<Long> update(CandidateEvaluationRequest request);
    List<CandidateEvaluationDetail> getByJobAdCandidate(Long jobAdCandidateId);
}
