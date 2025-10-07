package com.cvconnect.service;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;

import java.util.List;

public interface JobAdProcessCandidateService {
    void create(List<JobAdProcessCandidateDto> dtos);
}
