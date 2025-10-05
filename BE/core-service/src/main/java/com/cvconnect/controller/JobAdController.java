package com.cvconnect.controller;

import com.cvconnect.dto.jobAd.JobAdRequest;
import com.cvconnect.service.JobAdService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-ad")
public class JobAdController {
    @Autowired
    private JobAdService jobAdService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @Operation(summary = "Create Job Ad")
    @PreAuthorize("hasAnyAuthority('ORG_JOB_AD:ADD')")
    public ResponseEntity<Response<IDResponse<Long>>> createJobAd(@Valid @RequestBody JobAdRequest request) {
        return ResponseUtils.success(jobAdService.create(request),
                localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }
}
