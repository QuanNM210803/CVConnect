package com.cvconnect.controller;

import com.cvconnect.service.JobConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-config")
public class JobConfigController {
    @Autowired
    private JobConfigService jobConfigService;
}
