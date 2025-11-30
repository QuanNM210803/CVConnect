package com.cvconnect.service;

import com.cvconnect.dto.candidate.CandidateDto;

import java.util.Map;

public interface CandidateService {
    void createCandidate(CandidateDto dto);
    CandidateDto getCandidate(Long userId);
    Long numberOfNewCandidate(Map<String, Object> filter);
}
