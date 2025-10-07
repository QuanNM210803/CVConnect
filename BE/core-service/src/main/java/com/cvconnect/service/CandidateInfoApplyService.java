package com.cvconnect.service;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

public interface CandidateInfoApplyService {
    FilterResponse<CandidateInfoApplyDto> filter(CandidateInfoApplyFilterRequest request);
    CandidateInfoApplyDto getById(Long candidateInfoApplyId);
    List<Long> create(List<CandidateInfoApplyDto> dtos);
}
