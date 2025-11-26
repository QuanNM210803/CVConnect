package com.cvconnect.service.impl;

import com.cvconnect.dto.JobConfigDto;
import com.cvconnect.entity.JobConfig;
import com.cvconnect.repository.JobConfigRepository;
import com.cvconnect.service.JobConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobConfigServiceImpl implements JobConfigService {
    @Autowired
    private JobConfigRepository jobConfigRepository;

    @Override
    public List<JobConfigDto> getAllJobs() {
        List<JobConfig> jobConfigs = jobConfigRepository.findAll();
        return jobConfigs.stream()
                .filter(entity -> Boolean.TRUE.equals(entity.getIsActive()))
                .map(entity -> JobConfigDto.builder()
                        .id(entity.getId())
                        .jobName(entity.getJobName())
                        .description(entity.getDescription())
                        .scheduleType(entity.getScheduleType())
                        .expression(entity.getExpression())
                        .isActive(entity.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }
}
