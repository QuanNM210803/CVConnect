package com.cvconnect.controller;

import com.cvconnect.service.JobAdProcessCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-ad-process-candidate")
public class JobAdProcessCandidateController {
    @Autowired
    private JobAdProcessCandidateService jobAdProcessCandidateService;
}
