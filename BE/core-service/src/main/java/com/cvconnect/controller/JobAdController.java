package com.cvconnect.controller;

import com.cvconnect.dto.jobAd.JobAdOrgFilterRequest;
import com.cvconnect.dto.jobAd.JobAdOrgFilterResponse;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAd.JobAdRequest;
import com.cvconnect.service.JobAdService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/process/{jobAdId}")
    @Operation(summary = "Get Job Ad by Job Ad Process ID")
    @PreAuthorize("hasAnyAuthority('ORG_JOB_AD:VIEW')")
    public ResponseEntity<Response<List<JobAdProcessDto>>> getProcessByJobAdId(@PathVariable Long jobAdId) {
        return ResponseUtils.success(jobAdService.getProcessByJobAdId(jobAdId));
    }

    @GetMapping("/org/filter")
    @Operation(summary = "Filter Job Ads for Organization")
    @PreAuthorize("hasAnyAuthority('ORG_JOB_AD:VIEW')")
    public ResponseEntity<Response<FilterResponse<JobAdOrgFilterResponse>>> filterJobAdsForOrg(@Valid @ModelAttribute JobAdOrgFilterRequest request) {
        return ResponseUtils.success(jobAdService.filterJobAdsForOrg(request));
    }
}
