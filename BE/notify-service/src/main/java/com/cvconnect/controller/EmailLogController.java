package com.cvconnect.controller;

import com.cvconnect.service.EmailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-log")
public class EmailLogController {
    @Autowired
    private EmailLogService emailLogService;
}
