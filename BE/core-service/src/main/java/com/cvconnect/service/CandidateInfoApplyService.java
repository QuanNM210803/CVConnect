package com.cvconnect.service;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;
import java.util.Map;

public interface CandidateInfoApplyService {
    FilterResponse<CandidateInfoApplyDto> filter(CandidateInfoApplyFilterRequest request);
    CandidateInfoApplyDto getById(Long candidateInfoApplyId);
    List<Long> create(List<CandidateInfoApplyDto> dtos);
    Boolean validateCandidateInfoInProcess(List<Long> candidateInfoIds, Long jobAdProcessId);
    Map<Long, CandidateInfoApplyDto> getByIds(List<Long> candidateInfoIds);
    List<CandidateInfoApplyDto> getByCalendarId(Long calendarId);
    List<CandidateInfoApplyDto> getCandidateInCurrentProcess(Long jobAdProcessId);
}
