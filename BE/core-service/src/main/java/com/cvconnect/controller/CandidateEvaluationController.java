package com.cvconnect.controller;

import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationDetail;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationDto;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationRequest;
import com.cvconnect.entity.CandidateEvaluation;
import com.cvconnect.service.CandidateEvaluationService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate-evaluation")
public class CandidateEvaluationController {
    @Autowired
    private CandidateEvaluationService candidateEvaluationService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @Operation(summary = "Create a new candidate evaluation")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<IDResponse<Long>>> create(@Valid @RequestBody CandidateEvaluationRequest request) {
        return ResponseUtils.success(candidateEvaluationService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PostMapping("/update/{evaluationId}")
    @Operation(summary = "Create a new candidate evaluation")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<IDResponse<Long>>> update(@PathVariable Long evaluationId, @Valid @RequestBody CandidateEvaluationRequest request) {
        request.setId(evaluationId);
        return ResponseUtils.success(candidateEvaluationService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @GetMapping("/get-by-job-ad-candidate/{jobAdCandidateId}")
    @Operation(summary = "Create a new candidate evaluation")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<List<CandidateEvaluationDetail>>> getByJobAdCandidate(@PathVariable Long jobAdCandidateId) {
        return ResponseUtils.success(candidateEvaluationService.getByJobAdCandidate(jobAdCandidateId));
    }
}
