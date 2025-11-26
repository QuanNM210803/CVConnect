package com.cvconnect.service;

import com.cvconnect.dto.jobAdLevel.JobAdLevelDto;

import java.util.List;

public interface JobAdLevelService {
    void create(List<JobAdLevelDto> dtos);
}
