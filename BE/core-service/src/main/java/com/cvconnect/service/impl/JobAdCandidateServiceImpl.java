package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdCandidateRepository;
import com.cvconnect.service.JobAdCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdCandidateServiceImpl implements JobAdCandidateService {
    @Autowired
    private JobAdCandidateRepository jobAdCandidateRepository;
}
