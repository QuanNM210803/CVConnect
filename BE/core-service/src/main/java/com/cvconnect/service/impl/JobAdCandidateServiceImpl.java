package com.cvconnect.service.impl;

import com.cvconnect.common.ReplacePlaceholder;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.common.DataReplacePlaceholder;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.common.NotificationDto;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAdCandidate.ApplyRequest;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterRequest;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterResponse;
import com.cvconnect.dto.jobAdCandidate.JobAdProcessCandidateDto;
import com.cvconnect.entity.JobAdCandidate;
import com.cvconnect.enums.*;
import com.cvconnect.repository.JobAdCandidateRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.SendEmailDto;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.service.SendEmailService;
import nmquan.commonlib.utils.KafkaUtils;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private RestTemplateClient restTemplateClient;
    @Autowired
    private ReplacePlaceholder replacePlaceholder;
    @Autowired
    private KafkaUtils kafkaUtils;

    @Override
    @Transactional
    public IDResponse<Long> apply(ApplyRequest request, MultipartFile cvFile) {
        // validate
        Long userId = WebUtils.getCurrentUserId();
        request.setCandidateId(userId);
        this.validateApply(request, cvFile);
        JobAdDto jobAdDto = this.validateJobAd(request.getJobAdId());

        // save candidate info apply if not exist
        Long candidateInfoApplyId = request.getCandidateInfoApplyId();
        if(candidateInfoApplyId == null){
            CoreServiceUtils.validateDocumentFileInput(cvFile);
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
        CandidateInfoApplyDto candidateInfoApplyDto = candidateInfoApplyService.getById(candidateInfoApplyId);
        if(jobAdDto.getIsAutoSendEmail()){
            EmailTemplateDto emailTemplateDto = restTemplateClient.getEmailTemplateById(jobAdDto.getEmailTemplateId());
            UserDto userDto = restTemplateClient.getUser(jobAdDto.getHrContactId());
            if(ObjectUtils.isEmpty(emailTemplateDto) ||
                    ObjectUtils.isEmpty(userDto) ||
                    ObjectUtils.isEmpty(candidateInfoApplyDto)){
                throw new AppException(CommonErrorCode.ERROR);
            }
            String emailHr = userDto.getEmail();
            String emailCandidate = candidateInfoApplyDto.getEmail();
            String subject = emailTemplateDto.getSubject();

            // replace placeholders
            String template = emailTemplateDto.getBody();
            List<String> placeholders = emailTemplateDto.getPlaceholderCodes();
            DataReplacePlaceholder dataReplacePlaceholder = DataReplacePlaceholder.builder()
                    .positionId(jobAdDto.getPositionId())
                    .jobAdName(jobAdDto.getTitle())
                    .jobAdProcessName(ProcessTypeEnum.APPLY.name())
                    .orgId(jobAdDto.getOrgId())
                    .candidateName(candidateInfoApplyDto.getFullName())
                    .hrName(userDto.getFullName())
                    .hrEmail(emailHr)
                    .hrPhone(userDto.getPhoneNumber())
                    .build();
            String body = replacePlaceholder.replacePlaceholder(template, placeholders, dataReplacePlaceholder);
            SendEmailDto sendEmailDto = SendEmailDto.builder()
                    .sender(emailHr)
                    .recipients(List.of(emailCandidate))
                    .subject(subject)
                    .body(body)
                    .orgId(jobAdDto.getOrgId())
                    .build();
            sendEmailService.sendEmailWithBody(sendEmailDto);
        }

        // send notification to hr
        NotifyTemplate template = NotifyTemplate.CANDIDATE_APPLY_JOB_AD;
        NotificationDto notification = NotificationDto.builder()
                .title(String.format(template.getTitle()))
                .message(String.format(template.getMessage(), candidateInfoApplyDto.getFullName(), jobAdDto.getTitle()))
                .senderId(userId)
                .receiverIds(List.of(jobAdDto.getHrContactId()))
                .type(Constants.NotificationType.USER)
                .redirectUrl(Constants.Path.JOB_AD_CANDIDATE_DETAIL + "/" + userId)
                .build();
        kafkaUtils.sendWithJson(Constants.KafkaTopic.NOTIFICATION, notification);

        return IDResponse.<Long>builder()
                .id(jobAdCandidate.getId())
                .build();
    }

    @Override
    public FilterResponse<CandidateFilterResponse> filter(CandidateFilterRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long participantId = null;
        List<String> role = WebUtils.getCurrentRole();
        if(!role.contains(Constants.RoleCode.ORG_ADMIN)){
            participantId = WebUtils.getCurrentUserId();
            request.setHrContactId(null);
        }

        // todo: nho viet them cau lenh check nguoi tham gia la HR va interviewer
        Page<CandidateInfoApplyProjection> page = jobAdCandidateRepository.filter(request, orgId, participantId, request.getPageable());

        return null;
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

        boolean appliedJobAd = jobAdCandidateRepository.existsByJobAdIdAndCandidateId(request.getJobAdId(), request.getCandidateId());
        if(appliedJobAd){
            throw new AppException(CoreErrorCode.CANDIDATE_DUPLICATE_APPLY);
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
