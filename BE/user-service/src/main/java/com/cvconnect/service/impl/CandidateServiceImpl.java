package com.cvconnect.service.impl;

import com.cvconnect.dto.candidate.CandidateDto;
import com.cvconnect.entity.Candidate;
import com.cvconnect.repository.CandidateRepository;
import com.cvconnect.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public void createCandidate(CandidateDto dto) {
        Candidate candidate = new Candidate();
        candidate.setUserId(dto.getUserId());
        candidateRepository.save(candidate);
    }
}
