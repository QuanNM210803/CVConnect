package com.cvconnect.service;

import com.cvconnect.dto.jobAd.JobAdProcessDto;

import java.util.List;

public interface JobAdProcessService {
    void create(List<JobAdProcessDto> dtos);
    List<JobAdProcessDto> getByJobAdId(Long jobAdId);
    JobAdProcessDto getById(Long id);
}
