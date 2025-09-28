package com.cvconnect.controller;

import com.cvconnect.service.JobAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-ad")
public class JobAdController {
    @Autowired
    private JobAdService jobAdService;
}
