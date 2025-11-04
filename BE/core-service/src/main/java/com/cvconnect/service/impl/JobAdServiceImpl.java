package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.common.NotificationDto;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.enums.CurrencyTypeDto;
import com.cvconnect.dto.enums.JobAdStatusDto;
import com.cvconnect.dto.enums.JobTypeDto;
import com.cvconnect.dto.enums.SalaryTypeDto;
import com.cvconnect.dto.internal.response.EmailConfigDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.jobAd.*;
import com.cvconnect.dto.jobAdLevel.JobAdLevelDto;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.positionProcess.PositionProcessRequest;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.JobAd;
import com.cvconnect.enums.*;
import com.cvconnect.repository.JobAdRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.dto.request.FilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.KafkaUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class JobAdServiceImpl implements JobAdService {
    @Autowired
    private JobAdRepository jobAdRepository;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private JobAdCareerService jobAdCareerService;
    @Autowired
    private JobAdWorkLocationService jobAdWorkLocationService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private OrgAddressService orgAddressService;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private KafkaUtils kafkaUtils;
    @Autowired
    private LevelService levelService;
    @Autowired
    private JobAdLevelService jobAdLevelService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DepartmentService departmentService;

    private static final String JOB_AD_CODE_PREFIX = "JD-";

    @Override
    @Transactional
    public IDResponse<Long> create(JobAdRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        request.setOrgId(orgId);
        this.validateCreate(request);

        Long suffixMax = jobAdRepository.getSuffixCodeMax(request.getOrgId(), JOB_AD_CODE_PREFIX);
        suffixMax = suffixMax == null ? 0 : suffixMax;

        JobAd jobAd = new JobAd();
        jobAd.setCode(JOB_AD_CODE_PREFIX + (suffixMax + 1));
        jobAd.setTitle(request.getTitle());
        jobAd.setOrgId(request.getOrgId());
        jobAd.setPositionId(request.getPositionId());
        jobAd.setJobType(request.getJobType().name());
        jobAd.setDueDate(request.getDueDate());
        jobAd.setQuantity(request.getQuantity());
        jobAd.setSalaryType(request.getSalaryType().name());
        jobAd.setSalaryFrom(request.getSalaryFrom());
        jobAd.setSalaryTo(request.getSalaryTo());
        jobAd.setCurrencyType(request.getCurrencyType().name());
        jobAd.setKeyword(request.getKeyword());
        jobAd.setDescription(request.getDescription());
        jobAd.setRequirement(request.getRequirement());
        jobAd.setBenefit(request.getBenefit());
        jobAd.setHrContactId(request.getHrContactId());
        jobAd.setJobAdStatus(request.getJobAdStatus().name());
        jobAd.setIsPublic(request.isPublic());
        jobAd.setIsAutoSendEmail(request.isAutoSendEmail());
        jobAd.setEmailTemplateId(request.getEmailTemplateId());
        jobAd.setIsRemote(request.isHasRemote());
        jobAd.setIsAllLevel(request.getIsAllLevel());
        if(!request.isPublic()){
            jobAd.setKeyCodeInternal(UUID.randomUUID().toString());
        }
        jobAdRepository.save(jobAd);

        if(!ObjectUtils.isEmpty(request.getCareerIds())){
            List<JobAdCareerDto> jobAdCareerDtos = request.getCareerIds().stream()
                    .map(careerId -> JobAdCareerDto.builder()
                            .jobAdId(jobAd.getId())
                            .careerId(careerId)
                            .build())
                    .collect(Collectors.toList());
            jobAdCareerService.create(jobAdCareerDtos);
        }

        if(!ObjectUtils.isEmpty(request.getWorkLocationIds())){
            List<JobAdWorkLocationDto> jobAdWorkLocationDtos = request.getWorkLocationIds().stream()
                    .map(workLocationId -> JobAdWorkLocationDto.builder()
                            .jobAdId(jobAd.getId())
                            .workLocationId(workLocationId)
                            .build())
                    .collect(Collectors.toList());
            jobAdWorkLocationService.create(jobAdWorkLocationDtos);
        }

        if(!ObjectUtils.isEmpty(request.getPositionProcess())){
            AtomicInteger sortOrder = new AtomicInteger(1);
            List<JobAdProcessDto> jobAdProcessDtos = request.getPositionProcess().stream()
                    .map(process -> JobAdProcessDto.builder()
                                .name(process.getName())
                                .sortOrder(sortOrder.getAndIncrement())
                                .jobAdId(jobAd.getId())
                                .processTypeId(process.getProcessTypeId())
                                .build()
                    )
                    .collect(Collectors.toList());
            jobAdProcessService.create(jobAdProcessDtos);
        }

        if(!request.getIsAllLevel()) {
            List<JobAdLevelDto> jobAdLevelDtos = request.getLevelIds().stream()
                    .map(levelId -> JobAdLevelDto.builder()
                            .jobAdId(jobAd.getId())
                            .levelId(levelId)
                            .build())
                    .collect(Collectors.toList());
            jobAdLevelService.create(jobAdLevelDtos);
        }

        // send notification to HR contact
        NotifyTemplate template = NotifyTemplate.JOB_AD_CREATED;
        NotificationDto notificationDto = NotificationDto.builder()
                .title(String.format(template.getTitle(), WebUtils.getCurrentFullName()))
                .message(String.format(template.getMessage(), jobAd.getTitle()))
                .senderId(WebUtils.getCurrentUserId())
                .receiverIds(List.of(jobAd.getHrContactId()))
                .type(Constants.NotificationType.USER)
                .redirectUrl(Constants.Path.JOB_AD + "?mode=view&targetId=" + jobAd.getId())
                .build();
        kafkaUtils.sendWithJson(Constants.KafkaTopic.NOTIFICATION, notificationDto);

        return IDResponse.<Long>builder()
                .id(jobAd.getId())
                .build();
    }

    @Override
    public JobAdDto findById(Long id) {
        JobAd jobAd = jobAdRepository.findById(id);
        if(ObjectUtils.isEmpty(jobAd)){
            return null;
        }
        return ObjectMapperUtils.convertToObject(jobAd, JobAdDto.class);
    }

    @Override
    public JobAdDto findByJobAdProcessId(Long jobAdProcessId) {
        JobAd jobAd = jobAdRepository.findByJobAdProcessId(jobAdProcessId);
        if(ObjectUtils.isEmpty(jobAd)){
            return null;
        }
        return ObjectMapperUtils.convertToObject(jobAd, JobAdDto.class);
    }

    @Override
    public List<JobAdProcessDto> getProcessByJobAdId(Long jobAdId) {
        return jobAdProcessService.getByJobAdId(jobAdId);
    }

    @Override
    public FilterResponse<JobAdOrgDetailResponse> filterJobAdsForOrg(JobAdOrgFilterRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        request.setOrgId(orgId);

        Long participantId = null;
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)){
            participantId = WebUtils.getCurrentUserId();
        }

        Page<JobAdOrgFilterProjection> page = jobAdRepository.filterJobAdsForOrg(request, request.getPageable(), participantId);

        Set<Long> hrContactIds = page.getContent().stream()
                .map(JobAdOrgFilterProjection::getHrContactId)
                .collect(Collectors.toSet());
        Map<Long, UserDto> hrContactMap = restTemplateClient.getUsersByIds(new ArrayList<>(hrContactIds));

        List<Long> jobAdIds = page.getContent().stream()
                .map(JobAdOrgFilterProjection::getId)
                .toList();
        Map<Long, List<JobAdProcessDto>> jobAdProcessMap = jobAdProcessService.getJobAdProcessByJobAdIds(jobAdIds);

        List<JobAdOrgDetailResponse> dtos = page.getContent().stream()
                .map(projection -> {
                    JobAdOrgDetailResponse dto = new JobAdOrgDetailResponse();
                    dto.setId(projection.getId());
                    dto.setCode(projection.getCode());
                    dto.setTitle(projection.getTitle());

                    PositionDto position = PositionDto.builder()
                            .id(projection.getPositionId())
                            .name(projection.getPositionName())
                            .build();
                    dto.setPosition(position);

                    DepartmentDto department = DepartmentDto.builder()
                            .id(projection.getDepartmentId())
                            .name(projection.getDepartmentName())
                            .build();
                    dto.setDepartment(department);

                    dto.setDueDate(projection.getDueDate());
                    dto.setQuantity(projection.getQuantity());
                    dto.setHrContact(hrContactMap.get(projection.getHrContactId()));
                    dto.setJobAdStatus(JobAdStatus.getJobAdStatusDto(projection.getJobAdStatus()));
                    dto.setIsPublic(projection.getIsPublic());
                    dto.setKeyCodeInternal(projection.getKeyCodeInternal());
                    dto.setCreatedBy(projection.getCreatedBy());
                    dto.setCreatedAt(projection.getCreatedAt());
                    dto.setUpdatedBy(projection.getUpdatedBy());
                    dto.setUpdatedAt(projection.getUpdatedAt());
                    dto.setJobAdProcess(jobAdProcessMap.get(projection.getId()));
                    return dto;
                })
                .toList();
        return PageUtils.toFilterResponse(page, dtos);
    }

    @Override
    @Transactional
    public void updateJobAdStatus(JobAdStatusRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long currentUserId = WebUtils.getCurrentUserId();
        JobAd jobAd = jobAdRepository.findById(request.getJobAdId());
        if(ObjectUtils.isEmpty(jobAd) || !jobAd.getOrgId().equals(orgId) || !jobAd.getHrContactId().equals(currentUserId)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }

        JobAdStatus currentStatus = JobAdStatus.getJobAdStatus(jobAd.getJobAdStatus());
        JobAdStatus newStatus = request.getStatus();
        if(currentStatus == newStatus){
            return;
        }
        if(newStatus.getLevel() < currentStatus.getLevel()){
            throw new AppException(CoreErrorCode.JOB_AD_STATUS_CANNOT_REVERT);
        }
        jobAd.setJobAdStatus(newStatus.name());
        jobAdRepository.save(jobAd);
    }

    @Override
    @Transactional
    public void updatePublicStatus(JobAdPublicStatusRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long currentUserId = WebUtils.getCurrentUserId();
        JobAd jobAd = jobAdRepository.findById(request.getJobAdId());
        if(ObjectUtils.isEmpty(jobAd) || !jobAd.getOrgId().equals(orgId) || !jobAd.getHrContactId().equals(currentUserId)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }

        if(jobAd.getIsPublic().equals(request.getIsPublic())){
            return;
        }
        jobAd.setIsPublic(request.getIsPublic());
        if(!request.getIsPublic() && ObjectUtils.isEmpty(jobAd.getKeyCodeInternal())){
            jobAd.setKeyCodeInternal(UUID.randomUUID().toString());
        }
        jobAdRepository.save(jobAd);
    }

    @Override
    public JobAdOrgDetailResponse getJobAdOrgDetail(Long jobAdId) {
        Long orgId = restTemplateClient.validOrgMember();

        Long participantId = null;
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)){
            participantId = WebUtils.getCurrentUserId();
        }

        JobAd jobAd = jobAdRepository.getJobAdOrgDetailById(jobAdId, orgId, participantId);
        if(ObjectUtils.isEmpty(jobAd)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }

        JobAdOrgDetailResponse dto = new JobAdOrgDetailResponse();
        dto.setId(jobAd.getId());
        dto.setCode(jobAd.getCode());
        dto.setTitle(jobAd.getTitle());

        PositionDto position = positionService.findById(jobAd.getPositionId());
        dto.setPosition(position);

        if(!ObjectUtils.isEmpty(position) && !ObjectUtils.isEmpty(position.getDepartmentId())){
            DepartmentDto department = departmentService.findById(position.getDepartmentId());
            dto.setDepartment(department);
        }
        dto.setDueDate(jobAd.getDueDate());
        dto.setQuantity(jobAd.getQuantity());

        UserDto hrContact = restTemplateClient.getUser(jobAd.getHrContactId());
        dto.setHrContact(hrContact);

        JobAdStatusDto jobAdStatus = JobAdStatus.getJobAdStatusDto(jobAd.getJobAdStatus());
        dto.setJobAdStatus(jobAdStatus);

        dto.setIsPublic(jobAd.getIsPublic());
        dto.setKeyCodeInternal(jobAd.getKeyCodeInternal());
        dto.setCreatedBy(jobAd.getCreatedBy());
        dto.setCreatedAt(jobAd.getCreatedAt());
        dto.setUpdatedBy(jobAd.getUpdatedBy());
        dto.setUpdatedAt(jobAd.getUpdatedAt());

        Map<Long, List<JobAdProcessDto>> jobAdProcessMap = jobAdProcessService.getJobAdProcessByJobAdIds(List.of(jobAdId));
        dto.setJobAdProcess(jobAdProcessMap.get(jobAdId));

        JobTypeDto jobType = JobType.getJobTypeDto(jobAd.getJobType());
        dto.setJobType(jobType);

        SalaryTypeDto salaryType = SalaryType.getSalaryTypeDto(jobAd.getSalaryType());
        dto.setSalaryType(salaryType);
        dto.setSalaryFrom(jobAd.getSalaryFrom());
        dto.setSalaryTo(jobAd.getSalaryTo());

        CurrencyTypeDto currencyType = CurrencyType.getCurrencyTypeDto(jobAd.getCurrencyType());
        dto.setCurrencyType(currencyType);

        dto.setIsRemote(jobAd.getIsRemote());
        List<OrgAddressDto> workLocations = orgAddressService.getByJobAdId(jobAdId);
        dto.setWorkLocations(workLocations);

        dto.setIsAllLevel(jobAd.getIsAllLevel());
        List<LevelDto> levels = levelService.getLevelsByJobAdId(jobAdId);
        dto.setLevels(levels);

        dto.setKeyword(jobAd.getKeyword());
        dto.setDescription(jobAd.getDescription());
        dto.setRequirement(jobAd.getRequirement());
        dto.setBenefit(jobAd.getBenefit());

        dto.setIsAutoSendEmail(jobAd.getIsAutoSendEmail());
        dto.setEmailTemplateId(jobAd.getEmailTemplateId());

        return dto;
    }

    @Override
    @Transactional
    public IDResponse<Long> update(JobAdUpdateRequest request) {
        Long orgId = restTemplateClient.validOrgMember();

        JobAd jobAd = jobAdRepository.findById(request.getId());
        if(ObjectUtils.isEmpty(jobAd) || !jobAd.getOrgId().equals(orgId)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }

        // validate dueDate
        if(!request.getDueDate().equals(jobAd.getDueDate())){
            if(request.getDueDate().isBefore(Instant.now())) {
                throw new AppException(CoreErrorCode.DUE_DATE_MUST_BE_IN_FUTURE);
            }
        }

        // update fields
        jobAd.setTitle(request.getTitle());
        jobAd.setDueDate(request.getDueDate());
        jobAd.setQuantity(request.getQuantity());
        jobAd.setKeyword(request.getKeyword());
        jobAd.setDescription(request.getDescription());
        jobAd.setRequirement(request.getRequirement());
        jobAd.setBenefit(request.getBenefit());

        if(request.getIsAutoSendEmail() == null){
            throw new AppException(CoreErrorCode.IS_AUTO_SEND_EMAIL_REQUIRED);
        }
        if(!request.getIsAutoSendEmail()){
            jobAd.setIsAutoSendEmail(Boolean.FALSE);
            jobAd.setEmailTemplateId(null);
        } else {
            if(request.getEmailTemplateId() == null){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_ID_REQUIRED);
            }

            EmailConfigDto emailConfigDto = restTemplateClient.getEmailConfigByOrg();
            if(ObjectUtils.isEmpty(emailConfigDto)){
                throw new AppException(CoreErrorCode.EMAIL_CONFIG_NOT_FOUND);
            }

            List<EmailTemplateDto> response = restTemplateClient.getEmailTemplateByOrgId(orgId);
            boolean existsEmailTemplate = response.stream()
                    .anyMatch(dto ->
                            dto.getId().equals(request.getEmailTemplateId()) && dto.getIsActive()
                    );
            if(!existsEmailTemplate){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
            }

            jobAd.setIsAutoSendEmail(Boolean.TRUE);
            jobAd.setEmailTemplateId(request.getEmailTemplateId());
        }

        jobAdRepository.save(jobAd);
        return IDResponse.<Long>builder()
                .id(jobAd.getId())
                .build();
    }

    @Override
    public FilterResponse<JobAdDto> getJobAdsByParticipantId(FilterRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long participantId = null;
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)){
            participantId = WebUtils.getCurrentUserId();
        }
        Page<JobAd> page = jobAdRepository.getJobAdsByParticipantId(orgId, participantId, request.getPageable());
        List<JobAdDto> dtos = page.getContent().stream()
                .map(jobAd -> JobAdDto.builder()
                        .id(jobAd.getId())
                        .code(jobAd.getCode())
                        .title(jobAd.getTitle())
                        .build())
                .collect(Collectors.toList());
        return PageUtils.toFilterResponse(page, dtos);
    }

    private void validateCreate(JobAdRequest request) {
        // validate orgId, positionId
        boolean exists = jobAdRepository.existsByOrgIdAndPositionId(request.getOrgId(), request.getPositionId());
        if(!exists){
            throw new AppException(CoreErrorCode.ORG_POSITION_LEVEL_NOT_FOUND);
        }

        // validate workLocationIds
        if(!ObjectUtils.isEmpty(request.getWorkLocationIds())){
            List<OrgAddressDto> orgAddressDtos = orgAddressService.getByOrgIdAndIds(request.getOrgId(), request.getWorkLocationIds());
            if(orgAddressDtos.size() != request.getWorkLocationIds().size()){
                throw new AppException(CoreErrorCode.WORK_LOCATION_NOT_FOUND);
            }
        }

        // dueDate must be in the future
        if(request.getDueDate().isBefore(Instant.now())) {
            throw new AppException(CoreErrorCode.DUE_DATE_MUST_BE_IN_FUTURE);
        }

        // validate salaryType, salaryFrom, salaryTo
        if(SalaryType.RANGE.equals(request.getSalaryType())){
            if(request.getSalaryFrom() == null || request.getSalaryTo() == null
                    || request.getSalaryFrom() <= 0 || request.getSalaryTo() <= 0
                    || request.getSalaryFrom() > request.getSalaryTo()){
                throw new AppException(CoreErrorCode.SALARY_FROM_TO_INVALID);
            }
        } else {
            if(request.getSalaryFrom() != null || request.getSalaryTo() != null){
                request.setSalaryFrom(null);
                request.setSalaryTo(null);
            }
        }

        // validate hrContactId
        Boolean hrContactExists = restTemplateClient.checkOrgUserRole(request.getHrContactId(), Constants.RoleCode.HR, request.getOrgId());
        if(!hrContactExists){
            throw new AppException(CoreErrorCode.HR_CONTACT_NOT_FOUND);
        }

        // validate jobAdStatus: DRAFT or OPEN
        if(!JobAdStatus.OPEN.equals(request.getJobAdStatus())){
            throw new AppException(CoreErrorCode.JOB_AD_STATUS_INVALID);
        }

        // validate emailTemplateId if isAutoSendEmail = true
        if(request.isAutoSendEmail()){
            if(request.getEmailTemplateId() == null){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_ID_REQUIRED);
            }

            EmailConfigDto emailConfigDto = restTemplateClient.getEmailConfigByOrg();
            if(ObjectUtils.isEmpty(emailConfigDto)){
                throw new AppException(CoreErrorCode.EMAIL_CONFIG_NOT_FOUND);
            }

            List<EmailTemplateDto> response = restTemplateClient.getEmailTemplateByOrgId(request.getOrgId());
            boolean existsEmailTemplate = response.stream()
                    .anyMatch(dto ->
                            dto.getId().equals(request.getEmailTemplateId()) && dto.getIsActive()
                    );
            if(!existsEmailTemplate){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
            }
        }

        // validate positionProcess
        List<PositionProcessRequest> positionProcessRequests = request.getPositionProcess();
        PositionProcessRequest firstProcessRequest = positionProcessRequests.get(0);
        ProcessTypeDto firstProcessType = processTypeService.detail(firstProcessRequest.getProcessTypeId());
        if(!ProcessTypeEnum.APPLY.name().equals(firstProcessType.getCode())){
            throw new AppException(CoreErrorCode.FIRST_PROCESS_MUST_BE_APPLY);
        }
        PositionProcessRequest endProcessRequest = positionProcessRequests.get(positionProcessRequests.size() -1);
        ProcessTypeDto endProcessType = processTypeService.detail(endProcessRequest.getProcessTypeId());
        if(!ProcessTypeEnum.ONBOARD.name().equals(endProcessType.getCode())){
            throw new AppException(CoreErrorCode.LAST_PROCESS_MUST_BE_ONBOARD);
        }

        // validate level
        if(request.getIsAllLevel() == null || !request.getIsAllLevel()) {
            if(ObjectUtils.isEmpty(request.getLevelIds())) {
                throw new AppException(CoreErrorCode.LEVEL_REQUIRED);
            }
            List<LevelDto> levelDtos = levelService.getByIds(request.getLevelIds());
            if(levelDtos.size() != request.getLevelIds().size()) {
                throw new AppException(CoreErrorCode.LEVEL_NOT_FOUND);
            }
            request.setIsAllLevel(Boolean.FALSE);
        }
    }
}
