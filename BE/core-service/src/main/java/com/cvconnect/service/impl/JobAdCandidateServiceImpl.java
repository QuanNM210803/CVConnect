package com.cvconnect.service.impl;

import com.cvconnect.common.ReplacePlaceholder;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.attachFile.AttachFileDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoDetail;
import com.cvconnect.dto.candidateSummaryOrg.CandidateSummaryOrgDto;
import com.cvconnect.dto.common.DataReplacePlaceholder;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.common.NotificationDto;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.jobAdCandidate.*;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
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
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            if(!ObjectUtils.isEmpty(emailTemplateDto)){
                UserDto userDto = restTemplateClient.getUser(jobAdDto.getHrContactId());
                if(ObjectUtils.isEmpty(userDto) || ObjectUtils.isEmpty(candidateInfoApplyDto)){
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
                        .candidateInfoId(candidateInfoApplyId)
                        .jobAdId(jobAdCandidate.getJobAdId())
                        .orgId(jobAdDto.getOrgId())
                        .emailTemplateId(jobAdDto.getEmailTemplateId())
                        .build();
                sendEmailService.sendEmailWithBody(sendEmailDto);
            }
        }

        // send notification to hr
        NotifyTemplate template = NotifyTemplate.CANDIDATE_APPLY_JOB_AD;
        NotificationDto notification = NotificationDto.builder()
                .title(String.format(template.getTitle()))
                .message(String.format(template.getMessage(), candidateInfoApplyDto.getFullName(), jobAdDto.getTitle()))
                .senderId(userId)
                .receiverIds(List.of(jobAdDto.getHrContactId()))
                .type(Constants.NotificationType.USER)
                .redirectUrl(Constants.Path.JOB_AD_CANDIDATE_DETAIL + "/" + candidateInfoApplyId)
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
        if(Objects.equals(request.getSortBy(), CommonConstants.DEFAULT_SORT_BY)){
            request.setSortBy("applyDate");
        }
        Page<CandidateInfoApplyProjection> page = jobAdCandidateRepository.filter(request, orgId, participantId, request.getPageable());
        List<CandidateFilterResponse> data = page.getContent().stream()
                .map(projection -> CandidateFilterResponse.builder()
                        .candidateInfo(
                                CandidateInfoApplyDto.builder()
                                        .id(projection.getId())
                                        .fullName(projection.getFullName())
                                        .email(projection.getEmail())
                                        .phone(projection.getPhone())
                                        .candidateSummaryOrg(CandidateSummaryOrgDto.builder()
                                                .level(LevelDto.builder()
                                                        .id(projection.getLevelId())
                                                        .levelName(projection.getLevelName())
                                                        .build())
                                                .build())
                                        .build()
                        )
                        .numOfApply(projection.getNumOfApply())
                        .build())
                .toList();

        Map<Long, CandidateFilterResponse> candidateInfoMap = data.stream()
                .collect(Collectors.toMap(
                        item -> item.getCandidateInfo().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        List<Long> candidateInfoIds = new ArrayList<>(candidateInfoMap.keySet());
        List<CandidateFilterProjection> jobAdCandidates = jobAdCandidateRepository.findAllByCandidateInfoIds(request, candidateInfoIds, orgId, participantId);

        // get Hr contacts
        List<Long> hrIds = jobAdCandidates.stream()
                .map(CandidateFilterProjection::getHrContactId)
                .distinct()
                .toList();
        Map<Long, UserDto> hrContacts = restTemplateClient.getUsersByIds(hrIds);

        for(CandidateFilterProjection projection : jobAdCandidates){
            CandidateFilterResponse response = candidateInfoMap.get(projection.getCandidateInfoId());
            if(response != null){
                JobAdCandidateDto jobAdCandidateDto = JobAdCandidateDto.builder()
                        .candidateStatus(projection.getCandidateStatus())
                        .applyDate(projection.getApplyDate())
                        .jobAd(JobAdDto.builder()
                                .id(projection.getJobAdId())
                                .title(projection.getJobAdTitle())
                                .hrContactName(hrContacts.get(projection.getHrContactId()) != null ?
                                        hrContacts.get(projection.getHrContactId()).getUsername() : null)
                                .build())
                        .currentRound(ProcessTypeDto.builder()
                                .id(projection.getProcessTypeId())
                                .code(projection.getProcessTypeCode())
                                .name(projection.getProcessTypeName())
                                .build())
                        .build();
                if(response.getJobAdCandidates() == null){
                    response.setJobAdCandidates(new ArrayList<>());
                }
                response.getJobAdCandidates().add(jobAdCandidateDto);
            }
        }

        List<CandidateFilterResponse> result = new ArrayList<>(candidateInfoMap.values());
        return PageUtils.toFilterResponse(page, result);
    }

    @Override
    @Transactional
    public CandidateInfoDetail candidateDetail(Long candidateInfoId) {
        Long orgId = restTemplateClient.validOrgMember();
        Long participantId = null;
        List<String> role = WebUtils.getCurrentRole();
        if(!role.contains(Constants.RoleCode.ORG_ADMIN)){
            participantId = WebUtils.getCurrentUserId();
        }
        CandidateInfoApplyProjection projection = jobAdCandidateRepository.getCandidateInfoDetailProjection(candidateInfoId, orgId, participantId);
        if(ObjectUtils.isEmpty(projection)){
            throw new AppException(CoreErrorCode.CANDIDATE_INFO_APPLY_NOT_FOUND);
        }
        UserDto candidate = restTemplateClient.getUser(projection.getCandidateId());

        String secureUrl = null;
        if(candidate.getAvatarId() != null) {
            List<AttachFileDto> files = attachFileService.getAttachFiles(List.of(candidate.getAvatarId()));
            if (!ObjectUtils.isEmpty(files)) {
                secureUrl = files.get(0).getSecureUrl();
            }
        }

        CandidateInfoApplyDto candidateInfoApply = CandidateInfoApplyDto.builder()
                                    .id(projection.getId())
                                    .fullName(projection.getFullName())
                                    .email(projection.getEmail())
                                    .phone(projection.getPhone())
                                    .attachFile(AttachFileDto.builder()
                                            .id(projection.getCvFileId())
                                            .secureUrl(projection.getCvFileUrl())
                                            .build()
                                    )
                                    .coverLetter(projection.getCoverLetter())
                                    .candidateSummaryOrg(CandidateSummaryOrgDto.builder()
                                            .level(LevelDto.builder()
                                                    .id(projection.getLevelId())
                                                    .levelName(projection.getLevelName())
                                                    .build())
                                            .skill(projection.getSkill())
                                            .build())
                                    .avatarUrl(secureUrl)
                                    .build();

        List<JobAdCandidateProjection> jobAdCandidates = jobAdCandidateRepository.getJobAdCandidatesByCandidateInfoId(candidateInfoId, orgId, participantId);

        // get Hr contacts
        List<Long> hrIds = jobAdCandidates.stream()
                .map(JobAdCandidateProjection::getHrContactId)
                .distinct()
                .toList();
        Map<Long, UserDto> hrContacts = restTemplateClient.getUsersByIds(hrIds);

        Map<Long, List<JobAdCandidateProjection>> groupedByCandidate = jobAdCandidates.stream()
                .collect(Collectors.groupingBy(JobAdCandidateProjection::getJobAdCandidateId, LinkedHashMap::new, Collectors.toList()));

        List<JobAdCandidateDto> jobAdCandidateDtos = groupedByCandidate.values().stream()
                .map(projections -> {
                    JobAdCandidateProjection first = projections.get(0);

                    JobAdCandidateDto dto = JobAdCandidateDto.builder()
                            .id(first.getJobAdCandidateId())
                            .candidateStatus(first.getCandidateStatus())
                            .applyDate(first.getApplyDate())
                            .onboardDate(first.getOnboardDate())
                            .eliminateReason(EliminateReasonEnum.getEliminateReasonEnumDto(first.getEliminateReasonType()))
                            .eliminateReasonDetail(first.getEliminateReasonDetail())
                            .eliminateDate(first.getEliminateDate())
                            .jobAd(JobAdDto.builder()
                                    .id(first.getJobAdId())
                                    .title(first.getJobAdTitle())
                                    .hrContactId(first.getHrContactId())
                                    .hrContactName(hrContacts.get(first.getHrContactId()) != null ?
                                            hrContacts.get(first.getHrContactId()).getFullName() : null)
                                    .positionId(first.getPositionId())
                                    .positionName(first.getPositionName())
                                    .departmentId(first.getDepartmentId())
                                    .departmentName(first.getDepartmentName())
                                    .departmentCode(first.getDepartmentCode())
                                    .keyCodeInternal(first.getKeyCodeInternal())
                                    .build())
                            .build();

                    List<JobAdProcessCandidateDto> jobAdProcessCandidateDtos = projections.stream()
                            .map(p -> JobAdProcessCandidateDto.builder()
                                    .id(p.getJobAdProcessCandidateId())
                                    .jobAdProcessId(p.getJobAdProcessId())
                                    .processName(p.getProcessName())
                                    .isCurrentProcess(p.getIsCurrentProcess())
                                    .actionDate(p.getActionDate())
                                    .build())
                            .collect(Collectors.toList());

                    dto.setJobAdProcessCandidates(jobAdProcessCandidateDtos);
                    return dto;
                })
                .toList();

        // update candidate status to VIEWED_CV if current user is hr contact and status is APPLIED
        Long currentUserId = WebUtils.getCurrentUserId();
        if(hrIds.contains(currentUserId)){
            for(JobAdCandidateDto jobAdCandidateDto : jobAdCandidateDtos){
                if(jobAdCandidateDto.getJobAd().getHrContactId().equals(currentUserId) &&
                        CandidateStatus.APPLIED.name().equals(jobAdCandidateDto.getCandidateStatus())){
                    jobAdCandidateRepository.updateCandidateStatus(jobAdCandidateDto.getId(), CandidateStatus.VIEWED_CV.name());
                }
            }
        }

        return CandidateInfoDetail.builder()
                .candidateInfo(candidateInfoApply)
                .jobAdCandidates(jobAdCandidateDtos)
                .build();
    }

    @Override
    public boolean checkCandidateInfoInOrg(Long candidateInfoId, Long orgId, Long hrContactId) {
        return jobAdCandidateRepository.existsByCandidateInfoIdAndOrgIdAndHrContactId(candidateInfoId, orgId, hrContactId);
    }

    @Override
    @Transactional
    public void changeCandidateProcess(ChangeCandidateProcessRequest request) {
        Long toJobAdProcessCandidateId = request.getToJobAdProcessCandidateId();
        JobAdProcessCandidateDto toProcessCandidate = jobAdProcessCandidateService.findById(toJobAdProcessCandidateId);
        if(ObjectUtils.isEmpty(toProcessCandidate)){
            throw new AppException(CoreErrorCode.PROCESS_TYPE_NOT_FOUND);
        }

        // check authorization
        Long orgId = restTemplateClient.validOrgMember();
        Long hrContactId = WebUtils.getCurrentUserId();
        this.checkAuthorizedChangeProcess(toProcessCandidate.getJobAdCandidateId(), orgId, hrContactId);

        // check candidate reject
        Long jobAdCandidateId = toProcessCandidate.getJobAdCandidateId();
        JobAdCandidate jobAdCandidate = jobAdCandidateRepository.findById(jobAdCandidateId)
                .orElseThrow(() -> new AppException(CommonErrorCode.ERROR));
        if(CandidateStatus.REJECTED.name().equals(jobAdCandidate.getCandidateStatus())){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ELIMINATED);
        }

        // check candidate already onboarded
        Boolean checkOnboarded = jobAdCandidateRepository.existsByCandidateInfoAndOrg(jobAdCandidate.getCandidateInfoId(), orgId, CandidateStatus.ONBOARDED.name());
        if(checkOnboarded){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ONBOARDED);
        }

        // check valid process order change
        Boolean checkProcessOrder = jobAdProcessCandidateService.validateProcessOrderChange(toJobAdProcessCandidateId, jobAdCandidateId);
        if(!checkProcessOrder){
            throw new AppException(CoreErrorCode.INVALID_PROCESS_TYPE_CHANGE);
        }

        // update job ad candidate status
        JobAdProcessDto jobAdProcessDto = jobAdProcessService.getById(toProcessCandidate.getJobAdProcessId());
        if(ObjectUtils.isEmpty(jobAdProcessDto)){
            throw new AppException(CoreErrorCode.PROCESS_TYPE_NOT_FOUND);
        }
        ProcessTypeDto processTypeDto = jobAdProcessDto.getProcessType();
        if (!ProcessTypeEnum.APPLY.name().equals(processTypeDto.getCode()) &&
                !ProcessTypeEnum.ONBOARD.name().equals(processTypeDto.getCode())) {
            jobAdCandidate.setCandidateStatus(CandidateStatus.IN_PROGRESS.name());
        } else if (ProcessTypeEnum.ONBOARD.name().equals(processTypeDto.getCode())) {
            if(request.getOnboardDate() == null){
                throw new AppException(CoreErrorCode.ONBOARD_DATE_REQUIRED);
            }
            if(request.getOnboardDate().isBefore(ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant())){
                throw new AppException(CoreErrorCode.ONBOARD_DATE_INVALID);
            }
            jobAdCandidate.setCandidateStatus(CandidateStatus.WAITING_ONBOARDING.name());
            jobAdCandidate.setOnboardDate(request.getOnboardDate());
        }
        jobAdCandidateRepository.save(jobAdCandidate);

        // update process candidates
        List<JobAdProcessCandidateDto> dtos = jobAdProcessCandidateService.findByJobAdCandidateId(jobAdCandidateId);
        Instant now = ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant();
        for (JobAdProcessCandidateDto dto : dtos) {
            boolean isCurrent = dto.getId().equals(toJobAdProcessCandidateId);
            dto.setIsCurrentProcess(isCurrent);
            if (isCurrent) {
                dto.setActionDate(now);
            }
        }
        jobAdProcessCandidateService.create(dtos);

        // send email to candidate
        if(request.isSendEmail()){
            String subject;
            String template;
            List<String> placeholders;

            // get email template
            Long emailTemplateId = request.getEmailTemplateId();
            if(emailTemplateId != null){
                EmailTemplateDto emailTemplateDto = restTemplateClient.getEmailTemplateById(emailTemplateId);
                if(ObjectUtils.isEmpty(emailTemplateDto)){
                    throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
                }
                subject = emailTemplateDto.getSubject();
                template = emailTemplateDto.getBody();
                placeholders = emailTemplateDto.getPlaceholderCodes();
            } else {
                CoreServiceUtils.validateManualEmail(request.getSubject(), request.getTemplate());
                subject = request.getSubject();
                template = request.getTemplate();
                placeholders = request.getPlaceholders();
            }

            // get data to replace placeholder
            CandidateInfoApplyDto candidateInfo = candidateInfoApplyService.getById(jobAdCandidate.getCandidateInfoId());
            UserDto hrContact = restTemplateClient.getUser(hrContactId);
            JobAdDto jobAd = jobAdService.findById(jobAdCandidate.getJobAdId());
            if(ObjectUtils.isEmpty(hrContact) || ObjectUtils.isEmpty(candidateInfo) || ObjectUtils.isEmpty(jobAd)){
                throw new AppException(CommonErrorCode.ERROR);
            }

            // replace placeholders
            DataReplacePlaceholder dataReplacePlaceholder = DataReplacePlaceholder.builder()
                    .positionId(jobAd.getPositionId())
                    .jobAdName(jobAd.getTitle())
                    .jobAdProcessName(jobAdProcessDto.getName())
                    .orgId(jobAd.getOrgId())
                    .candidateName(candidateInfo.getFullName())
                    .hrName(hrContact.getFullName())
                    .hrEmail(hrContact.getEmail())
                    .hrPhone(hrContact.getPhoneNumber())
                    .build();
            if(request.getOnboardDate() != null){
                dataReplacePlaceholder.setExamStartTime(request.getOnboardDate());
            }
            String body = replacePlaceholder.replacePlaceholder(template, placeholders, dataReplacePlaceholder);

            // send email
            SendEmailDto sendEmailDto = SendEmailDto.builder()
                    .sender(hrContact.getEmail())
                    .recipients(List.of(candidateInfo.getEmail()))
                    .subject(subject)
                    .body(body)
                    .candidateInfoId(candidateInfo.getId())
                    .jobAdId(jobAd.getId())
                    .orgId(jobAd.getOrgId())
                    .emailTemplateId(emailTemplateId)
                    .build();
            sendEmailService.sendEmailWithBody(sendEmailDto);
        }
    }

    @Override
    @Transactional
    public void eliminateCandidate(EliminateCandidateRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long hrContactId = WebUtils.getCurrentUserId();
        this.checkAuthorizedChangeProcess(request.getJobAdCandidateId(), orgId, hrContactId);

        JobAdCandidate jobAdCandidate = jobAdCandidateRepository.findById(request.getJobAdCandidateId()).orElseThrow(
                () -> new AppException(CommonErrorCode.DATA_NOT_FOUND)
        );
        if(CandidateStatus.REJECTED.name().equals(jobAdCandidate.getCandidateStatus())){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ELIMINATED);
        }
        jobAdCandidate.setCandidateStatus(CandidateStatus.REJECTED.name());
        jobAdCandidate.setEliminateReasonType(request.getReason().name());
        jobAdCandidate.setEliminateReasonDetail(request.getReasonDetail());
        jobAdCandidate.setEliminateDate(ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant());
        jobAdCandidateRepository.save(jobAdCandidate);

        if(request.isSendEmail()){
            String subject;
            String template;
            List<String> placeholders;

            // get email template
            Long emailTemplateId = request.getEmailTemplateId();
            if(emailTemplateId != null){
                EmailTemplateDto emailTemplateDto = restTemplateClient.getEmailTemplateById(emailTemplateId);
                if(ObjectUtils.isEmpty(emailTemplateDto)){
                    throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
                }
                subject = emailTemplateDto.getSubject();
                template = emailTemplateDto.getBody();
                placeholders = emailTemplateDto.getPlaceholderCodes();
            } else {
                CoreServiceUtils.validateManualEmail(request.getSubject(), request.getTemplate());
                subject = request.getSubject();
                template = request.getTemplate();
                placeholders = request.getPlaceholders();
            }

            // get data to replace placeholder
            CandidateInfoApplyDto candidateInfo = candidateInfoApplyService.getById(jobAdCandidate.getCandidateInfoId());
            UserDto hrContact = restTemplateClient.getUser(hrContactId);
            JobAdDto jobAd = jobAdService.findById(jobAdCandidate.getJobAdId());
            if(ObjectUtils.isEmpty(hrContact) || ObjectUtils.isEmpty(candidateInfo) || ObjectUtils.isEmpty(jobAd)){
                throw new AppException(CommonErrorCode.ERROR);
            }

            // replace placeholders
            DataReplacePlaceholder dataReplacePlaceholder = DataReplacePlaceholder.builder()
                    .positionId(jobAd.getPositionId())
                    .jobAdName(jobAd.getTitle())
                    .orgId(jobAd.getOrgId())
                    .candidateName(candidateInfo.getFullName())
                    .hrName(hrContact.getFullName())
                    .hrEmail(hrContact.getEmail())
                    .hrPhone(hrContact.getPhoneNumber())
                    .build();
            String body = replacePlaceholder.replacePlaceholder(template, placeholders, dataReplacePlaceholder);

            // send email
            SendEmailDto sendEmailDto = SendEmailDto.builder()
                    .sender(hrContact.getEmail())
                    .recipients(List.of(candidateInfo.getEmail()))
                    .subject(subject)
                    .body(body)
                    .candidateInfoId(candidateInfo.getId())
                    .jobAdId(jobAd.getId())
                    .orgId(jobAd.getOrgId())
                    .emailTemplateId(emailTemplateId)
                    .build();
            sendEmailService.sendEmailWithBody(sendEmailDto);
        }
    }

    @Override
    @Transactional
    public void changeOnboardDate(ChangeOnboardDateRequest request) {
        if(request.getNewOnboardDate().isBefore(ZonedDateTime.now(CommonConstants.ZONE.UTC).toInstant())){
            throw new AppException(CoreErrorCode.ONBOARD_DATE_INVALID);
        }

        // check authorization
        Long orgId = restTemplateClient.validOrgMember();
        Long hrContactId = WebUtils.getCurrentUserId();
        this.checkAuthorizedChangeProcess(request.getJobAdCandidateId(), orgId, hrContactId);

        // check candidate reject
        JobAdCandidate jobAdCandidate = jobAdCandidateRepository.findById(request.getJobAdCandidateId())
                .orElseThrow(() -> new AppException(CommonErrorCode.ERROR));
        if(CandidateStatus.REJECTED.name().equals(jobAdCandidate.getCandidateStatus())){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ELIMINATED);
        }

        // check candidate in onboard process
        Boolean isMatchCurrentProcess = jobAdProcessCandidateService.validateCurrentProcessTypeIs(jobAdCandidate.getId(), ProcessTypeEnum.ONBOARD.name());
        if(!isMatchCurrentProcess){
            throw new AppException(CoreErrorCode.CANDIDATE_NOT_IN_ONBOARD_PROCESS);
        }

        // check candidate already onboarded
        Boolean checkOnboarded = jobAdCandidateRepository.existsByCandidateInfoAndOrg(jobAdCandidate.getCandidateInfoId(), orgId, CandidateStatus.ONBOARDED.name());
        if(checkOnboarded){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ONBOARDED);
        }

        jobAdCandidate.setOnboardDate(request.getNewOnboardDate());
        jobAdCandidateRepository.save(jobAdCandidate);
    }

    @Override
    public void markOnboard(MarkOnboardRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long hrContactId = WebUtils.getCurrentUserId();

        // check authorization
        this.checkAuthorizedChangeProcess(request.getJobAdCandidateId(), orgId, hrContactId);

        // check candidate reject
        JobAdCandidate jobAdCandidate = jobAdCandidateRepository.findById(request.getJobAdCandidateId())
                .orElseThrow(() -> new AppException(CommonErrorCode.ERROR));
        if(CandidateStatus.REJECTED.name().equals(jobAdCandidate.getCandidateStatus())){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ELIMINATED);
        }

        // check candidate in onboard process
        Boolean isMatchCurrentProcess = jobAdProcessCandidateService.validateCurrentProcessTypeIs(jobAdCandidate.getId(), ProcessTypeEnum.ONBOARD.name());
        if(!isMatchCurrentProcess){
            throw new AppException(CoreErrorCode.CANDIDATE_NOT_IN_ONBOARD_PROCESS);
        }

        // check candidate already onboarded
        Boolean checkOnboarded = jobAdCandidateRepository.existsByCandidateInfoAndOrgAndNotJobAdCandidate(jobAdCandidate.getCandidateInfoId(), orgId, jobAdCandidate.getId(), CandidateStatus.ONBOARDED.name());
        if(checkOnboarded){
            throw new AppException(CoreErrorCode.CANDIDATE_ALREADY_ONBOARDED);
        }

        jobAdCandidate.setCandidateStatus(request.getIsOnboarded() ?
                CandidateStatus.ONBOARDED.name() : CandidateStatus.NOT_ONBOARDED.name());
        jobAdCandidateRepository.save(jobAdCandidate);
    }

    @Override
    public Boolean existsByJobAdCandidateIdAndHrContactId(Long jobAdCandidateId, Long hrContactId) {
        return jobAdCandidateRepository.existsByJobAdCandidateIdAndHrContactId(jobAdCandidateId, hrContactId);
    }

    @Override
    public Boolean existsByJobAdCandidateIdAndOrgId(Long jobAdCandidateId, Long orgId) {
        return jobAdCandidateRepository.existsByJobAdCandidateIdAndOrgId(jobAdCandidateId, orgId);
    }

    @Override
    public JobAdCandidateDto findById(Long jobAdCandidateId) {
        JobAdCandidate jobAdCandidate = jobAdCandidateRepository.findById(jobAdCandidateId).orElse(null);
        if(ObjectUtils.isEmpty(jobAdCandidate)){
            return null;
        }
        return ObjectMapperUtils.convertToObject(jobAdCandidate, JobAdCandidateDto.class);
    }

    @Override
    public void sendEmailToCandidate(SendEmailToCandidateRequest request) {
        // validate
        Long orgId = restTemplateClient.validOrgMember();
        JobAdDto jobAd = jobAdService.findById(request.getJobAdId());
        if(ObjectUtils.isEmpty(jobAd)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        if(!jobAd.getOrgId().equals(orgId)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        boolean existsCandidateInfo = jobAdCandidateRepository.existsByJobAdIdAndCandidateInfoId(request.getJobAdId(), request.getCandidateInfoId());
        if(!existsCandidateInfo){
            throw new AppException(CoreErrorCode.CANDIDATE_INFO_APPLY_NOT_FOUND);
        }

        String subject;
        String template;
        List<String> placeholders;

        // get email template
        Long emailTemplateId = request.getEmailTemplateId();
        if(emailTemplateId != null){
            EmailTemplateDto emailTemplateDto = restTemplateClient.getEmailTemplateById(emailTemplateId);
            if(ObjectUtils.isEmpty(emailTemplateDto)){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
            }
            subject = emailTemplateDto.getSubject();
            template = emailTemplateDto.getBody();
            placeholders = emailTemplateDto.getPlaceholderCodes();
        } else {
            CoreServiceUtils.validateManualEmail(request.getSubject(), request.getTemplate());
            subject = request.getSubject();
            template = request.getTemplate();
            placeholders = request.getPlaceholders();
        }

        // get data to replace placeholder
        CandidateInfoApplyDto candidateInfo = candidateInfoApplyService.getById(request.getCandidateInfoId());
        UserDto hrContact = restTemplateClient.getUser(jobAd.getHrContactId());
        JobAdProcessCandidateDto jobAdProcessCandidateDto = jobAdProcessCandidateService.getCurrentProcess(request.getJobAdId(), request.getCandidateInfoId());
        if(ObjectUtils.isEmpty(hrContact) || ObjectUtils.isEmpty(candidateInfo) || ObjectUtils.isEmpty(jobAdProcessCandidateDto)){
            throw new AppException(CommonErrorCode.ERROR);
        }

        // replace placeholders
        DataReplacePlaceholder dataReplacePlaceholder = DataReplacePlaceholder.builder()
                .positionId(jobAd.getPositionId())
                .jobAdName(jobAd.getTitle())
                .jobAdProcessName(jobAdProcessCandidateDto.getProcessName())
                .orgId(jobAd.getOrgId())
                .candidateName(candidateInfo.getFullName())
                .hrName(hrContact.getFullName())
                .hrEmail(hrContact.getEmail())
                .hrPhone(hrContact.getPhoneNumber())
                .build();
        String body = replacePlaceholder.replacePlaceholder(template, placeholders, dataReplacePlaceholder);

        // send email
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .sender(hrContact.getEmail())
                .recipients(List.of(candidateInfo.getEmail()))
                .subject(subject)
                .body(body)
                .candidateInfoId(candidateInfo.getId())
                .jobAdId(jobAd.getId())
                .orgId(jobAd.getOrgId())
                .emailTemplateId(emailTemplateId)
                .build();
        sendEmailService.sendEmailWithBody(sendEmailDto);
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

    private void checkAuthorizedChangeProcess(Long jobAdCandidateId, Long orgId, Long hrContactId) {
        List<String> role = WebUtils.getCurrentRole();
        boolean checkAuthorized;
        if (role.contains(Constants.RoleCode.ORG_ADMIN)) {
            checkAuthorized = jobAdCandidateRepository.existsByJobAdCandidateIdAndOrgId(jobAdCandidateId, orgId);
        } else {
            checkAuthorized = jobAdCandidateRepository.existsByJobAdCandidateIdAndHrContactId(jobAdCandidateId, hrContactId);
        }
        if (!checkAuthorized) {
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
    }
}
