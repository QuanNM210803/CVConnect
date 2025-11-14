package com.cvconnect.service.impl;

import com.cvconnect.repository.JobAdStatisticRepository;
import com.cvconnect.service.JobAdStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdStatisticServiceImpl implements JobAdStatisticService {
    @Autowired
    private JobAdStatisticRepository jobAdStatisticRepository;
}
