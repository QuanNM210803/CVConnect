package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationDetail;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationDto;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationProjection;
import com.cvconnect.dto.candidateEvaluation.CandidateEvaluationRequest;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAdCandidate.JobAdCandidateDto;
import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.CandidateEvaluation;
import com.cvconnect.repository.CandidateEvaluationRepository;
import com.cvconnect.service.CandidateEvaluationService;
import com.cvconnect.service.JobAdCandidateService;
import com.cvconnect.service.JobAdProcessCandidateService;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CandidateEvaluationServiceImpl implements CandidateEvaluationService {
    @Autowired
    private CandidateEvaluationRepository candidateEvaluationRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private JobAdCandidateService jobAdCandidateService;
    @Autowired
    private JobAdProcessCandidateService jobAdProcessCandidateService;

    @Override
    public IDResponse<Long> create(CandidateEvaluationRequest request) {
        Long jobAdCandidateId = request.getJobAdCandidateId();
        if(jobAdCandidateId == null){
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }

        List<JobAdProcessCandidateDto> processCandidates = jobAdProcessCandidateService.findByJobAdCandidateId(jobAdCandidateId);
        JobAdProcessCandidateDto currentProcessCandidate = null;
        for (int i = processCandidates.size() - 1; i >= 0; i--) {
            JobAdProcessCandidateDto dto = processCandidates.get(i);
            if (Boolean.TRUE.equals(dto.getIsCurrentProcess())) {
                currentProcessCandidate = dto;
                break;
            }
        }
        if (currentProcessCandidate == null) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }

        // check authorization
        Long orgId = restTemplateClient.validOrgMember();
        Long currentUserId = WebUtils.getCurrentUserId();
        List<String> role = WebUtils.getCurrentRole();
        boolean checkAuthorized;
        if (role.contains(Constants.RoleCode.ORG_ADMIN)) {
            checkAuthorized = jobAdCandidateService.existsByJobAdCandidateIdAndOrgId(jobAdCandidateId, orgId);
        } else {
            checkAuthorized = jobAdCandidateService.existsByJobAdCandidateIdAndHrContactId(jobAdCandidateId, currentUserId);
            if(!checkAuthorized){
                // check interviewer
                JobAdCandidateDto jobAdCandidateDto = jobAdCandidateService.findById(jobAdCandidateId);
                List<Long> interviewerIds = candidateEvaluationRepository.getInterviewerByJobAdProcessAndCandidateInfoId(
                        currentProcessCandidate.getJobAdProcessId(), jobAdCandidateDto.getCandidateInfoId());
                checkAuthorized = interviewerIds.contains(currentUserId);
            }
        }
        if (!checkAuthorized) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        CandidateEvaluation candidateEvaluation = new CandidateEvaluation();
        candidateEvaluation.setJobAdProcessCandidateId(currentProcessCandidate.getId());
        candidateEvaluation.setEvaluatorId(currentUserId);
        candidateEvaluation.setComments(request.getComments());
        candidateEvaluation.setScore(request.getScore());
        candidateEvaluationRepository.save(candidateEvaluation);
        return IDResponse.<Long>builder()
                .id(candidateEvaluation.getId())
                .build();
    }

    @Override
    public IDResponse<Long> update(CandidateEvaluationRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long currentUserId = WebUtils.getCurrentUserId();

        CandidateEvaluation candidateEvaluation = candidateEvaluationRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(CommonErrorCode.DATA_NOT_FOUND)
        );
        if(!candidateEvaluation.getEvaluatorId().equals(currentUserId)){
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        candidateEvaluation.setComments(request.getComments());
        candidateEvaluation.setScore(request.getScore());
        candidateEvaluationRepository.save(candidateEvaluation);
        return IDResponse.<Long>builder()
                .id(candidateEvaluation.getId())
                .build();
    }

    @Override
    public List<CandidateEvaluationDetail> getByJobAdCandidate(Long jobAdCandidateId) {
        Long orgId = restTemplateClient.validOrgMember();
        Long currentUserId = WebUtils.getCurrentUserId();
        List<String> role = WebUtils.getCurrentRole();

        Long evaluatorId = null;
        if (role.contains(Constants.RoleCode.ORG_ADMIN)) {
            Boolean checkAuthorized = jobAdCandidateService.existsByJobAdCandidateIdAndOrgId(jobAdCandidateId, orgId);
            if (!checkAuthorized) {
                throw new AppException(CommonErrorCode.UNAUTHENTICATED);
            }
        } else {
            Boolean checkHr = jobAdCandidateService.existsByJobAdCandidateIdAndHrContactId(jobAdCandidateId, currentUserId);
            if(!checkHr){
                evaluatorId = currentUserId;
            }
        }

        List<CandidateEvaluationProjection> projections = candidateEvaluationRepository.getByJobAdCandidateId(jobAdCandidateId, evaluatorId);
        List<Long> evaluatorIds = projections.stream()
                .map(CandidateEvaluationProjection::getEvaluatorId)
                .distinct()
                .toList();
        Map<Long, UserDto> evaluatorMap = restTemplateClient.getUsersByIds(evaluatorIds);

        List<CandidateEvaluationDetail> result = projections.stream()
                .collect(Collectors.groupingBy(CandidateEvaluationProjection::getJobAdProcessId))
                .entrySet().stream()
                .map(entry -> {
                    Long processId = entry.getKey();
                    List<CandidateEvaluationProjection> group = entry.getValue();

                    JobAdProcessDto processDto = new JobAdProcessDto();
                    processDto.setId(processId);
                    processDto.setName(group.get(0).getJobAdProcessName());

                    List<CandidateEvaluationDto> evaluations = group.stream().map(p -> {
                        UserDto evaluator = evaluatorMap.get(p.getEvaluatorId());
                        CandidateEvaluationDto dto = new CandidateEvaluationDto();
                        dto.setId(p.getId());
                        dto.setJobAdProcessCandidateId(p.getJobAdProcessCandidateId());
                        dto.setEvaluatorId(p.getEvaluatorId());
                        if(evaluator != null){
                            dto.setEvaluatorName(evaluator.getFullName());
                            dto.setEvaluatorUsername(evaluator.getUsername());
                        }
                        dto.setComments(p.getComments());
                        dto.setScore(p.getScore());
                        dto.setCreatedAt(p.getCreatedAt());
                        dto.setUpdatedAt(p.getUpdatedAt());
                        return dto;
                    }).collect(Collectors.toList());

                    CandidateEvaluationDetail detail = new CandidateEvaluationDetail();
                    detail.setJobAdProcess(processDto);
                    detail.setEvaluations(evaluations);
                    return detail;
                })
                .toList();

        return result;
    }
}
