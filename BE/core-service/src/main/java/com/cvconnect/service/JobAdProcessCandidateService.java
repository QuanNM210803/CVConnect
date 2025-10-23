package com.cvconnect.service;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;

import java.util.List;

public interface JobAdProcessCandidateService {
    void create(List<JobAdProcessCandidateDto> dtos);
    List<JobAdProcessCandidateDto> findByJobAdCandidateId(Long jobAdCandidateId);
    JobAdProcessCandidateDto findById(Long jobAdCandidateId);
    Boolean validateProcessOrderChange(Long jobAdProcessCandidateId, Long jobAdCandidateId);
}
