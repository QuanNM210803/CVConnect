package com.cvconnect.service.impl;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAdCandidate.ApplyRequest;
import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.JobAdCandidate;
import com.cvconnect.enums.CandidateStatus;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.enums.JobAdStatus;
import com.cvconnect.enums.ProcessTypeEnum;
import com.cvconnect.repository.JobAdCandidateRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.service.RestTemplateService;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class JobAdCandidateServiceImpl implements JobAdCandidateService {
    @Autowired
    private JobAdCandidateRepository jobAdCandidateRepository;
    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private CandidateInfoApplyService candidateInfoApplyService;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private JobAdProcessCandidateService jobAdProcessCandidateService;
    @Autowired
    private JobAdService jobAdService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private RestTemplateService restTemplateService;
    @Value("${server.notify_service}")
    private String SERVER_NOTIFY_SERVICE;
    @Value("${server.user_service}")
    private String SERVER_USER_SERVICE;

    @Override
    @Transactional
    public IDResponse<Long> apply(ApplyRequest request, MultipartFile cvFile) {
        // validate
        request.setCandidateId(WebUtils.getCurrentUserId());
        this.validateApply(request, cvFile);
        JobAdDto jobAdDto = this.validateJobAd(request.getJobAdId());

        // save candidate info apply if not exist
        Long candidateInfoApplyId = request.getCandidateInfoApplyId();
        if(candidateInfoApplyId == null){
            MultipartFile[] files = new MultipartFile[]{cvFile};
            List<Long> cvFileIds = attachFileService.uploadFile(files);
            CandidateInfoApplyDto candidateInfoApplyDto = CandidateInfoApplyDto.builder()
                    .candidateId(request.getCandidateId())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .cvFileId(cvFileIds.get(0))
                    .coverLetter(request.getCoverLetter())
                    .build();
            List<Long> candidateInfoIds = candidateInfoApplyService.create(List.of(candidateInfoApplyDto));
            candidateInfoApplyId = candidateInfoIds.get(0);
        }

        // save job ad candidate
        Instant instantNow = ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant();

        JobAdCandidate jobAdCandidate = new JobAdCandidate();
        jobAdCandidate.setJobAdId(request.getJobAdId());
        jobAdCandidate.setCandidateInfoId(candidateInfoApplyId);
        jobAdCandidate.setApplyDate(instantNow);
        jobAdCandidate.setCandidateStatus(CandidateStatus.APPLIED.name());
        jobAdCandidateRepository.save(jobAdCandidate);


        // save job ad process candidate
        List<JobAdProcessDto> jobAdProcessDtos = jobAdProcessService.getByJobAdId(request.getJobAdId());
        List<JobAdProcessCandidateDto> jobAdProcessCandidateDtos = jobAdProcessDtos.stream()
                .map(process -> {
                    JobAdProcessCandidateDto dto = new JobAdProcessCandidateDto();
                    dto.setJobAdProcessId(process.getId());
                    dto.setJobAdCandidateId(jobAdCandidate.getId());
                    dto.setIsCurrentProcess(false);
                    if(ProcessTypeEnum.APPLY.name().equals(process.getProcessType().getCode())){
                        dto.setIsCurrentProcess(true);
                        dto.setActionDate(instantNow);
                    }
                    return dto;
                })
                .toList();
        jobAdProcessCandidateService.create(jobAdProcessCandidateDtos);

        // send email to candidate
        if(jobAdDto.getIsAutoSendEmail()){
            Response<EmailTemplateDto> emailTemplate = restTemplateService.getMethodRestTemplate(
                SERVER_NOTIFY_SERVICE + "/email-template/internal/get-by-id/{id}",
                    new ParameterizedTypeReference<Response<EmailTemplateDto>>() {},
                    jobAdDto.getEmailTemplateId()
            );
            Response<UserDto> hrContact = restTemplateService.getMethodRestTemplate(
                    SERVER_USER_SERVICE + "/user/internal/get-by-id/{id}",
                    new ParameterizedTypeReference<Response<UserDto>>() {},
                    jobAdDto.getHrContactId()
            );
            CandidateInfoApplyDto candidateInfoApplyDto = candidateInfoApplyService.getById(candidateInfoApplyId);
            if(ObjectUtils.isEmpty(emailTemplate.getData()) ||
                    ObjectUtils.isEmpty(hrContact.getData()) ||
                    ObjectUtils.isEmpty(candidateInfoApplyDto)){
                throw new AppException(CommonErrorCode.ERROR);
            }
            String emailCandidate = candidateInfoApplyDto.getEmail();
            String emailHr = hrContact.getData().getEmail();
            String subject = emailTemplate.getData().getSubject();

            // replace placeholders
            String body = emailTemplate.getData().getBody();
            List<String> placeholders = emailTemplate.getData().getPlaceholderCodes();
            for(String placeholder : placeholders){
//                if(placeholder.equals("{{candidate_name}}")){
//                    body = body.replace(placeholder, candidateInfoApplyDto.getFullName());
//                } else if(placeholder.equals("{{job_title}}")){
//                    body = body.replace(placeholder, jobAdDto.getTitle());
//                } else if(placeholder.equals("{{company_name}}")){
//                    body = body.replace(placeholder, jobAdDto.getCompanyName());
//                } else if(placeholder.equals("{{hr_contact_name}}")){
//                    body = body.replace(placeholder, hrContact.getData().getFullName());
//                } else if(placeholder.equals("{{hr_contact_email}}")){
//                    body = body.replace(placeholder, hrContact.getData().getEmail());
//                } else if(placeholder.equals("{{hr_contact_phone}}")){
//                    body = body.replace(placeholder, hrContact.getData().getPhone());
//                }
            }

            sendEmailService.sendEmailWithBody(emailHr, List.of(emailCandidate), null, subject, body, jobAdDto.getOrgId());
        }

        return IDResponse.<Long>builder()
                .id(jobAdCandidate.getId())
                .build();
    }

    private void validateApply(ApplyRequest request, MultipartFile cvFile) {
        Long candidateInfoApplyId = request.getCandidateInfoApplyId();
        if(ObjectUtils.isEmpty(candidateInfoApplyId)) {
            if(ObjectUtils.isEmpty(cvFile)) {
                throw new AppException(CoreErrorCode.CV_FILE_NOT_FOUND);
            }
            if(ObjectUtils.isEmpty(request.getFullName())){
                throw new AppException(CoreErrorCode.FULL_NAME_REQUIRED);
            }
            if(ObjectUtils.isEmpty(request.getEmail())){
                throw new AppException(CoreErrorCode.EMAIL_REQUIRED);
            }
            if(ObjectUtils.isEmpty(request.getPhone())){
                throw new AppException(CoreErrorCode.PHONE_REQUIRED);
            }
        } else {
            if(ObjectUtils.isEmpty(request.getCandidateId())) {
                throw new AppException(CoreErrorCode.CANDIDATE_NOT_FOUND);
            }
            CandidateInfoApplyDto candidateInfoApplyDto = candidateInfoApplyService.getById(candidateInfoApplyId);
            if(ObjectUtils.isEmpty(candidateInfoApplyDto) || !candidateInfoApplyDto.getCandidateId().equals(request.getCandidateId())) {
                throw new AppException(CoreErrorCode.CANDIDATE_INFO_APPLY_NOT_FOUND);
            }
        }
    }

    private JobAdDto validateJobAd(Long jobAdId) {
        JobAdDto jobAdDto = jobAdService.findById(jobAdId);
        if(ObjectUtils.isEmpty(jobAdDto)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        if(jobAdDto.getDueDate().isBefore(ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant())){
            throw new AppException(CoreErrorCode.JOB_AD_EXPIRED);
        }
        JobAdStatus status = JobAdStatus.getJobAdStatus(jobAdDto.getJobAdStatus());
        if(ObjectUtils.isEmpty(status) || !JobAdStatus.OPEN.equals(status)){
            throw new AppException(CoreErrorCode.JOB_AD_STOP_RECRUITMENT);
        }
        return jobAdDto;
    }
}
