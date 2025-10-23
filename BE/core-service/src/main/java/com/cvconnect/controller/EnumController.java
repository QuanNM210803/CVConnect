package com.cvconnect.controller;

import com.cvconnect.dto.enums.*;
import com.cvconnect.service.EnumService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/type")
public class EnumController {
    @Autowired
    private EnumService enumService;

    @GetMapping("/currency")
    @Operation(summary = "Get all currency types")
    public ResponseEntity<Response<List<CurrencyTypeDto>>> getCurrencyType() {
        return ResponseUtils.success(enumService.getCurrencyType());
    }

    @GetMapping("/job-ad-status")
    @Operation(summary = "Get all job ad status")
    public ResponseEntity<Response<List<JobAdStatusDto>>> getJobAdStatus() {
        return ResponseUtils.success(enumService.getJobAdStatus());
    }

    @GetMapping("/Job")
    @Operation(summary = "Get all job types")
    public ResponseEntity<Response<List<JobTypeDto>>> getJobType() {
        return ResponseUtils.success(enumService.getJobType());
    }

    @GetMapping("/salary")
    @Operation(summary = "Get all salary types")
    public ResponseEntity<Response<List<SalaryTypeDto>>> getSalaryType() {
        return ResponseUtils.success(enumService.getSalaryType());
    }

    @GetMapping("/eliminate-reason")
    @Operation(summary = "Get all eliminate reasons")
    public ResponseEntity<Response<List<EliminateReasonEnumDto>>> getEliminateReason() {
        return ResponseUtils.success(enumService.getEliminateReason());
    }
}
