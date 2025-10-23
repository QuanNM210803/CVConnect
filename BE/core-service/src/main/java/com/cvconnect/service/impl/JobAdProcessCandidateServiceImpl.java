package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.JobAdProcessCandidate;
import com.cvconnect.repository.JobAdProcessCandidateRepository;
import com.cvconnect.service.JobAdProcessCandidateService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class JobAdProcessCandidateServiceImpl implements JobAdProcessCandidateService {
    @Autowired
    private JobAdProcessCandidateRepository jobAdProcessCandidateRepository;

    @Override
    public void create(List<JobAdProcessCandidateDto> dtos) {
        List<JobAdProcessCandidate> entities = ObjectMapperUtils.convertToList(dtos, JobAdProcessCandidate.class);
        if(!entities.isEmpty()){
            jobAdProcessCandidateRepository.saveAll(entities);
        }
    }

    @Override
    public List<JobAdProcessCandidateDto> findByJobAdCandidateId(Long jobAdCandidateId) {
        List<JobAdProcessCandidate> entities = jobAdProcessCandidateRepository.findByJobAdCandidateId(jobAdCandidateId);
        if(!ObjectUtils.isEmpty(entities)){
            return List.of();
        }
        return ObjectMapperUtils.convertToList(entities, JobAdProcessCandidateDto.class);
    }

    @Override
    public JobAdProcessCandidateDto findById(Long jobAdCandidateId) {
        JobAdProcessCandidate entity = jobAdProcessCandidateRepository.findById(jobAdCandidateId).orElse(null);
        if(ObjectUtils.isEmpty(entity)){
            return null;
        }
        return ObjectMapperUtils.convertToObject(entity, JobAdProcessCandidateDto.class);
    }

    @Override
    public Boolean validateProcessOrderChange(Long jobAdProcessCandidateId, Long jobAdCandidateId) {
        return jobAdProcessCandidateRepository.validateProcessOrderChange(jobAdProcessCandidateId, jobAdCandidateId);
    }
}
