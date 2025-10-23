package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoDetail;
import com.cvconnect.dto.jobAdCandidate.*;
import com.cvconnect.service.JobAdCandidateService;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/job-ad-candidate")
public class JobAdCandidateController {
    @Autowired
    private JobAdCandidateService jobAdCandidateService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/apply")
    @Operation(summary = "Apply for a job ad")
    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    public ResponseEntity<Response<IDResponse<Long>>> apply(@Valid @RequestPart ApplyRequest request,
                                                            @RequestPart(required = false) MultipartFile cvFile) {
        return ResponseUtils.success(jobAdCandidateService.apply(request, cvFile), localizationUtils.getLocalizedMessage(Messages.APPLY_SUCCESS));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter job ad candidates")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<FilterResponse<CandidateFilterResponse>>> filter(@Valid @ModelAttribute CandidateFilterRequest request) {
        return ResponseUtils.success(jobAdCandidateService.filter(request));
    }

    @GetMapping("/candidate-detail/{candidateInfoId}")
    @Operation(summary = "Get candidate detail by candidateInfoId")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<CandidateInfoDetail>> getCandidateDetail(@PathVariable Long candidateInfoId) {
        return ResponseUtils.success(jobAdCandidateService.candidateDetail(candidateInfoId));
    }

    @PutMapping("/change-process")
    @Operation(summary = "Change candidate process status")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:UPDATE')")
    public ResponseEntity<Response<Void>> changeCandidateProcess(@Valid @RequestBody ChangeCandidateProcessRequest request) {
        jobAdCandidateService.changeCandidateProcess(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(Messages.CHANGE_CANDIDATE_PROCESS_SUCCESS));
    }

    @PutMapping("/eliminate-candidate")
    @Operation(summary = "Eliminate candidate from recruitment process")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:UPDATE')")
    public ResponseEntity<Response<Void>> eliminateCandidate(@Valid @RequestBody EliminateCandidateRequest request) {
        jobAdCandidateService.eliminateCandidate(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }
}
