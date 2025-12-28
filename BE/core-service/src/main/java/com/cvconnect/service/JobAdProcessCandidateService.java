package com.cvconnect.service;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;

import java.util.List;
import java.util.Map;

public interface JobAdProcessCandidateService {
    void create(List<JobAdProcessCandidateDto> dtos);
    List<JobAdProcessCandidateDto> findByJobAdCandidateId(Long jobAdCandidateId);
    JobAdProcessCandidateDto findById(Long jobAdProcessCandidateId);
    Boolean validateProcessOrderChange(Long jobAdProcessCandidateId, Long jobAdCandidateId);
    Boolean validateCurrentProcessTypeIs(Long jobAdCandidateId, String processTypeCode);
    JobAdProcessCandidateDto getCurrentProcess(Long jobAdId, Long candidateInfoId);
    Map<Long, List<JobAdProcessCandidateDto>> getDetailByJobAdCandidateIds(List<Long> jobAdCandidateIds);
}
