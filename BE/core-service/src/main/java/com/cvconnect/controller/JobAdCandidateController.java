package com.cvconnect.controller;

import com.cvconnect.service.JobAdCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-ad-candidate")
public class JobAdCandidateController {
    @Autowired
    private JobAdCandidateService jobAdCandidateService;
}
