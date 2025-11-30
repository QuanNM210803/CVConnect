package com.cvconnect.service.impl;

import com.cvconnect.dto.candidate.CandidateDto;
import com.cvconnect.entity.Candidate;
import com.cvconnect.repository.CandidateRepository;
import com.cvconnect.service.CandidateService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public CandidateDto getCandidate(Long userId) {
        Optional<Candidate> candidate = candidateRepository.findByUserId(userId);
        Candidate entity = candidate.orElse(null);
        if(entity == null) {
            return null;
        }
        return CandidateDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public Long numberOfNewCandidate(Map<String, Object> filter) {
        Instant startTime = Instant.ofEpochSecond(filter.get("startTime") instanceof Number
                ? ((Number) filter.get("startTime")).longValue()
                : Double.valueOf(filter.get("startTime").toString()).longValue());
        if(startTime == null) {
            throw new AppException(CommonErrorCode.INVALID_FORMAT);
        }
        Instant endTime = Instant.ofEpochSecond(filter.get("endTime") instanceof Number
                ? ((Number) filter.get("endTime")).longValue()
                : Double.valueOf(filter.get("endTime").toString()).longValue());

        return candidateRepository.numberOfNewCandidate(startTime, endTime);
    }
}
