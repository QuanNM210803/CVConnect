package com.cvconnect.service;

import com.cvconnect.dto.jobAd.JobAdProcessDto;

import java.util.List;
import java.util.Map;

public interface JobAdProcessService {
    void create(List<JobAdProcessDto> dtos);
    List<JobAdProcessDto> getByJobAdId(Long jobAdId);
    JobAdProcessDto getById(Long id);
    Boolean existByJobAdProcessIdAndOrgId(Long jobAdProcessId, Long orgId);
    Map<Long, List<JobAdProcessDto>> getJobAdProcessByJobAdIds(List<Long> jobAdIds);
}
