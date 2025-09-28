package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdRepository;
import com.cvconnect.service.JobAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdServiceImpl implements JobAdService {
    @Autowired
    private JobAdRepository jobAdRepository;
}
