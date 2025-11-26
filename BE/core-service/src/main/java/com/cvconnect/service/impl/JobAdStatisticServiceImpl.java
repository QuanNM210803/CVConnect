package com.cvconnect.service.impl;

import com.cvconnect.entity.JobAdStatistic;
import com.cvconnect.repository.JobAdStatisticRepository;
import com.cvconnect.service.JobAdStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobAdStatisticServiceImpl implements JobAdStatisticService {
    @Autowired
    private JobAdStatisticRepository jobAdStatisticRepository;

    @Override
    public void addViewStatistic(Long jobAdId) {
        JobAdStatistic jobAdStatistic = jobAdStatisticRepository.findByJobAdId(jobAdId);
        if(jobAdStatistic == null){
            jobAdStatistic = new JobAdStatistic();
            jobAdStatistic.setJobAdId(jobAdId);
            jobAdStatistic.setViewCount(1L);
        } else {
            jobAdStatistic.setViewCount(jobAdStatistic.getViewCount() + 1);
        }
        jobAdStatisticRepository.save(jobAdStatistic);
    }
}
