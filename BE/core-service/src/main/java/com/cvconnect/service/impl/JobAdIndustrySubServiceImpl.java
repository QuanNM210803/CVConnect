package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAd.JobAdIndustrySubDto;
import com.cvconnect.entity.JobAdIndustrySub;
import com.cvconnect.repository.JobAdIndustrySubRepository;
import com.cvconnect.service.JobAdIndustrySubService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdIndustrySubServiceImpl implements JobAdIndustrySubService {
    @Autowired
    private JobAdIndustrySubRepository jobAdIndustrySubRepository;

    @Override
    public void create(List<JobAdIndustrySubDto> dtos) {
        List<JobAdIndustrySub> entities = ObjectMapperUtils.convertToList(dtos, JobAdIndustrySub.class);
        if(!entities.isEmpty()){
            jobAdIndustrySubRepository.saveAll(entities);
        }
    }
}
