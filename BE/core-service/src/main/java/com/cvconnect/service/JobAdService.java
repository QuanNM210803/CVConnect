package com.cvconnect.service;

import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAd.JobAdRequest;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface JobAdService {
    IDResponse<Long> create(JobAdRequest request);
    JobAdDto findById(Long id);
    JobAdDto findByJobAdProcessId(Long jobAdProcessId);
    List<JobAdProcessDto> getProcessByJobAdId(Long jobAdId);
}
