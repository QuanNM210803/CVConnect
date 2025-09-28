package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdProcessRepository;
import com.cvconnect.service.JobAdProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdProcessServiceImpl implements JobAdProcessService {
    @Autowired
    private JobAdProcessRepository jobAdProcessRepository;
}
