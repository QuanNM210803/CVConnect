package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoDetail;
import com.cvconnect.dto.jobAdCandidate.*;
import com.cvconnect.service.JobAdCandidateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.annotation.InternalRequest;
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

import java.util.Map;

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

    @PutMapping("/change-onboard-date")
    @Operation(summary = "Change candidate onboard date")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:UPDATE')")
    public ResponseEntity<Response<Void>> changeOnboardDate(@Valid @RequestBody ChangeOnboardDateRequest request) {
        jobAdCandidateService.changeOnboardDate(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PutMapping("/mark-onboard")
    @Operation(summary = "Mark candidate as onboarded")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:UPDATE')")
    public ResponseEntity<Response<Void>> markOnboard(@Valid @RequestBody MarkOnboardRequest request) {
        jobAdCandidateService.markOnboard(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PostMapping("/send-email")
    @Operation(summary = "Send email to candidate")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:UPDATE')")
    public ResponseEntity<Response<Void>> sendEmailToCandidate(@Valid @RequestBody SendEmailToCandidateRequest request) {
        jobAdCandidateService.sendEmailToCandidate(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(Messages.SEND_EMAIL_SUCCESS));
    }

    @GetMapping("/job-ad-applied")
    @Operation(summary = "Get job ads applied by candidate")
    public ResponseEntity<Response<FilterResponse<JobAdCandidateDto>>> getJobAdsAppliedByCandidate(@Valid @ModelAttribute JobAdAppliedFilterRequest request) {
        return ResponseUtils.success(jobAdCandidateService.getJobAdsAppliedByCandidate(request));
    }

    @PostMapping("/internal/validate-create-conversation")
    @InternalRequest
    @Operation(summary = "Validate and get HR contact ID for creating conversation")
    public ResponseEntity<Response<Long>> validateAndGetHrContactId(@RequestBody Map<String, Object> body) {
        Long jobAdId = Long.valueOf(body.get("jobAdId").toString());
        Long candidateId = Long.valueOf(body.get("candidateId").toString());
        return ResponseUtils.success(jobAdCandidateService.validateAndGetHrContactId(jobAdId, candidateId));
    }

    @GetMapping("/conversation/view-candidate")
    @Operation(summary = "Job ad candidate conversation view for candidate")
    public ResponseEntity<Response<FilterResponse<JobAdCandidateDto>>> jobAdCandidateConversation(@Valid @ModelAttribute JobAdAppliedFilterRequest request) {
        return ResponseUtils.success(jobAdCandidateService.jobAdCandidateConversation(request));
    }

    @GetMapping("/conversation/view-organization")
    @Operation(summary = "Job ad candidate conversation view for organization")
    public ResponseEntity<Response<FilterResponse<JobAdCandidateDto>>> jobAdCandidateConversationForOrg(@Valid @ModelAttribute MyConversationWithFilter request) {
        return ResponseUtils.success(jobAdCandidateService.jobAdCandidateConversationForOrg(request));
    }

    @GetMapping("/list-onboard")
    @Operation(summary = "Get list of onboarded candidates")
    @PreAuthorize("hasAnyAuthority('ORG_CANDIDATE:VIEW')")
    public ResponseEntity<Response<FilterResponse<JobAdCandidateDto>>> getListOfOnboardedCandidates(@Valid @ModelAttribute CandidateOnboardFilterRequest request) {
        return ResponseUtils.success(jobAdCandidateService.getListOfOnboardedCandidates(request));
    }
}
