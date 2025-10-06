package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdProcessCandidateRepository;
import com.cvconnect.service.JobAdProcessCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdProcessCandidateServiceImpl implements JobAdProcessCandidateService {
    @Autowired
    private JobAdProcessCandidateRepository jobAdProcessCandidateRepository;
}
