package com.cvconnect.service;

import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface JobAdService {
    IDResponse<Long> create(JobAdRequest request);
    JobAdDto findById(Long id);
}
