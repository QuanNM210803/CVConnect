package com.cvconnect.controller;

import com.cvconnect.service.CandidateService;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @PostMapping("/internal/number-of-new-candidate")
    public ResponseEntity<Response<Long>> numberOfNewCandidate(@RequestBody Map<String, Object> filter) {
        return ResponseUtils.success(candidateService.numberOfNewCandidate(filter));
    }
}
