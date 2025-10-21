package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAd.JobAdCareerDto;
import com.cvconnect.entity.JobAdCareer;
import com.cvconnect.repository.JobAdCareerRepository;
import com.cvconnect.service.JobAdCareerService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdCareerServiceImpl implements JobAdCareerService {
    @Autowired
    private JobAdCareerRepository jobAdCareerRepository;

    @Override
    public void create(List<JobAdCareerDto> dtos) {
        List<JobAdCareer> entities = ObjectMapperUtils.convertToList(dtos, JobAdCareer.class);
        if(!entities.isEmpty()){
            jobAdCareerRepository.saveAll(entities);
        }
    }
}
