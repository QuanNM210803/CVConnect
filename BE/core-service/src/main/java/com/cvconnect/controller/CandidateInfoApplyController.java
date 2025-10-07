package com.cvconnect.controller;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import com.cvconnect.service.CandidateInfoApplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidate-info-apply")
public class CandidateInfoApplyController {
    @Autowired
    private CandidateInfoApplyService candidateInfoApplyService;

    @GetMapping("/filter")
    @Operation(summary = "Filter Candidate Info Apply")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Response<FilterResponse<CandidateInfoApplyDto>>> filter(@Valid @ModelAttribute CandidateInfoApplyFilterRequest request) {
        return ResponseUtils.success(candidateInfoApplyService.filter(request));
    }
}
