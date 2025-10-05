package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAd.JobAdWorkLocationDto;
import com.cvconnect.entity.JobAdWorkLocation;
import com.cvconnect.repository.JobAdWorkLocationRepository;
import com.cvconnect.service.JobAdWorkLocationService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdWorkLocationServiceImpl implements JobAdWorkLocationService {
    @Autowired
    private JobAdWorkLocationRepository jobAdWorkLocationRepository;

    @Override
    public void create(List<JobAdWorkLocationDto> dtos) {
        List<JobAdWorkLocation> entities = ObjectMapperUtils.convertToList(dtos, JobAdWorkLocation.class);
        if(!entities.isEmpty()){
            jobAdWorkLocationRepository.saveAll(entities);
        }
    }
}
