package com.cvconnect.service;

import com.cvconnect.dto.jobAd.JobAdCareerDto;

import java.util.List;

public interface JobAdCareerService {
    void create(List<JobAdCareerDto> dtos);
}
