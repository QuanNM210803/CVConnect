package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.entity.JobAdProcess;
import com.cvconnect.repository.JobAdProcessRepository;
import com.cvconnect.service.JobAdProcessService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdProcessServiceImpl implements JobAdProcessService {
    @Autowired
    private JobAdProcessRepository jobAdProcessRepository;

    @Override
    public void create(List<JobAdProcessDto> dtos) {
        List<JobAdProcess> entities = ObjectMapperUtils.convertToList(dtos, JobAdProcess.class);
        if(!entities.isEmpty()){
            jobAdProcessRepository.saveAll(entities);
        }
    }
}
