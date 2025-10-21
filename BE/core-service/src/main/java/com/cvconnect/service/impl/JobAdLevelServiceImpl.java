package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAdLevel.JobAdLevelDto;
import com.cvconnect.entity.JobAdLevel;
import com.cvconnect.repository.JobAdLevelRepository;
import com.cvconnect.service.JobAdLevelService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdLevelServiceImpl implements JobAdLevelService {
    @Autowired
    private JobAdLevelRepository jobAdLevelRepository;

    @Override
    public void create(List<JobAdLevelDto> dtos) {
        List<JobAdLevel> entities = ObjectMapperUtils.convertToList(dtos, JobAdLevel.class);
        jobAdLevelRepository.saveAll(entities);
    }
}
