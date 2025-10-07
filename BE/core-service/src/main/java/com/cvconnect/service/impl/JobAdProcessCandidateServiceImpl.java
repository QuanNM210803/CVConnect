package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.JobAdProcessCandidate;
import com.cvconnect.repository.JobAdProcessCandidateRepository;
import com.cvconnect.service.JobAdProcessCandidateService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
