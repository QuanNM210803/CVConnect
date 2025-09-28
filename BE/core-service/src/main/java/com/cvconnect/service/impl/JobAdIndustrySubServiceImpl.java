package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdIndustrySubRepository;
import com.cvconnect.service.JobAdIndustrySubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdIndustrySubServiceImpl implements JobAdIndustrySubService {
    @Autowired
    private JobAdIndustrySubRepository jobAdIndustrySubRepository;
}
