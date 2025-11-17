package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
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
import com.cvconnect.dto.level.LevelFilterRequest;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.WorkingLocationDto;
import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.positionProcess.PositionProcessRequest;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;
import com.cvconnect.entity.JobAd;
import com.cvconnect.enums.*;
import com.cvconnect.repository.JobAdRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.PageInfo;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    private CareerService careerService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private SearchHistoryOutsideService searchHistoryOutsideService;
    @Autowired
    private JobAdStatisticService jobAdStatisticService;

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
        if(ObjectUtils.isEmpty(jobAd) || !jobAd.getOrgId().equals(orgId)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)){
            if(!jobAd.getHrContactId().equals(currentUserId)) {
                throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
            }
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
        if(ObjectUtils.isEmpty(jobAd) || !jobAd.getOrgId().equals(orgId)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)){
            if(!jobAd.getHrContactId().equals(currentUserId)) {
                throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
            }
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

    @Override
    public JobAdOutsideDataFilter outsideDataFilter() {
        List<JobTypeDto> jobTypeDtos = JobType.getAll();

        CareerFilterRequest careerFilterRequest = new CareerFilterRequest();
        careerFilterRequest.setPageSize(Integer.MAX_VALUE);
        FilterResponse<CareerDto> careerDtos = careerService.filter(careerFilterRequest);
        List<CareerDto> careers = careerDtos.getData();

        LevelFilterRequest levelFilterRequest = new LevelFilterRequest();
        levelFilterRequest.setPageSize(Integer.MAX_VALUE);
        FilterResponse<LevelDto> levelDtos = levelService.filter(levelFilterRequest);
        List<LevelDto> levels = levelDtos.getData();

        return JobAdOutsideDataFilter.builder()
                .careers(careers)
                .levels(levels)
                .jobTypes(jobTypeDtos)
                .build();
    }

    @Override
    public JobAdOutsideFilterResponse<JobAdOutsideDetailResponse> filterJobAdsForOutside(JobAdOutsideFilterRequest request) {
        request.setPageSize(20); // default page size
        request.setSortBy(CoreServiceUtils.toSnakeCase(request.getSortBy()));
        Boolean isShowExpired = request.getOrgId() != null;

        Long userId = WebUtils.getCurrentUserId();
        if(userId != null){
            if(!ObjectUtils.isEmpty(request.getKeyword())){
                SearchHistoryOutsideDto dto = SearchHistoryOutsideDto.builder()
                        .userId(userId)
                        .keyword(request.getKeyword())
                        .build();
                searchHistoryOutsideService.create(dto);
            }
        }

        Pageable pageable = request.getPageable();
        List<JobAdProjection> jobAdProjections = jobAdRepository.filterJobAdsForOutsideFunction(
                request.getKeyword(),
                isShowExpired,
                request.getCareerIds() != null ? request.getCareerIds().toArray(new Long[0]) : null,
                request.getLevelIds() != null ? request.getLevelIds().toArray(new Long[0]) : null,
                request.getJobAdLocation(),
                request.getIsRemote(),
                request.getSalaryFrom() != null ? Math.toIntExact(request.getSalaryFrom()) : null,
                request.getSalaryTo() != null ? Math.toIntExact(request.getSalaryTo()) : null,
                request.getNegotiable(),
                request.getJobType() != null ? request.getJobType().name() : null,
                request.isSearchOrg(),
                request.getOrgId(),
                pageable.getPageSize(),
                (int) pageable.getOffset(),
                pageable.getSort().iterator().next().getProperty(),
                pageable.getSort().iterator().next().getDirection().name()
        );

        List<JobAdDto> jobAdDtos = ObjectMapperUtils.convertToList(jobAdProjections, JobAdDto.class);
        List<JobAdOutsideDetailResponse> dtos = this.buildJobAdOutsideByFilter(jobAdDtos);

        FilterResponse<JobAdOutsideDetailResponse> response = new FilterResponse<>();
        response.setData(dtos);
        response.setPageInfo(PageInfo.builder()
                        .pageIndex(pageable.getPageNumber())
                        .pageSize(dtos.size())
                .build());

        // count working location
        List<Object[]> locationData = jobAdRepository.getWorkingLocationByFilterFunction(
                request.getKeyword(),
                isShowExpired,
                request.getCareerIds() != null ? request.getCareerIds().toArray(new Long[0]) : null,
                request.getLevelIds() != null ? request.getLevelIds().toArray(new Long[0]) : null,
                request.getJobAdLocation(),
                request.getIsRemote(),
                request.getSalaryFrom() != null ? Math.toIntExact(request.getSalaryFrom()) : null,
                request.getSalaryTo() != null ? Math.toIntExact(request.getSalaryTo()) : null,
                request.getNegotiable(),
                request.getJobType() != null ? request.getJobType().name() : null,
                request.isSearchOrg(),
                request.getOrgId()
        );
        Set<Long> remoteJobIds = new HashSet<>();
        Map<String, Set<Long>> provinceToJobIds = new HashMap<>();

        for (Object[] row : locationData) {
            Long jobId = ((Number) row[0]).longValue();
            Boolean isRemote = (Boolean) row[1];
            String province = (String) row[2];

            if (Boolean.TRUE.equals(isRemote)) {
                remoteJobIds.add(jobId);
            }

            if (province != null && !province.isBlank()) {
                provinceToJobIds.computeIfAbsent(province.trim(), k -> new HashSet<>()).add(jobId);
            }
        }

        List<WorkingLocationDto> locations = provinceToJobIds.entrySet().stream()
                .map(e -> WorkingLocationDto.builder()
                        .province(e.getKey())
                        .jobAdCount((long) e.getValue().size())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        if (!remoteJobIds.isEmpty()) {
            locations.add(WorkingLocationDto.builder()
                    .province("Remote")
                    .jobAdCount((long) remoteJobIds.size())
                    .build());
        }

        locations.sort(Comparator.comparing(WorkingLocationDto::getJobAdCount).reversed());

        return JobAdOutsideFilterResponse.<JobAdOutsideDetailResponse>builder()
                .data(response)
                .locations(locations)
                .build();
    }

    @Override
    public JobAdOutsideDetailResponse detailOutside(Long jobAdId, String keyCodeInternal) {
        JobAd ja = jobAdRepository.findById(jobAdId);
        if(ObjectUtils.isEmpty(ja)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        if(!ja.getIsPublic()){
            if(ObjectUtils.isEmpty(keyCodeInternal) || !keyCodeInternal.equals(ja.getKeyCodeInternal())){
                throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
            }
        }
        JobAdDto jobAd = ObjectMapperUtils.convertToObject(ja, JobAdDto.class);
        JobAdOutsideDetailResponse response = this.buildJobAdOutsideDetail(jobAd);

        // save view statistic
        jobAdStatisticService.addViewStatistic(jobAdId);

        return response;
    }

    @Override
    public List<JobAdOutsideDetailResponse> listRelateOutside(Long jobAdId) {
        JobAd ja = jobAdRepository.findById(jobAdId);
        if(ObjectUtils.isEmpty(ja)){
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        List<JobAd> relatedJobAds = jobAdRepository.findRelatedJobAds(ja.getTitle(), jobAdId);
        List<JobAdDto> jobAdDtos = ObjectMapperUtils.convertToList(relatedJobAds, JobAdDto.class);

        return this.buildJobAdOutsideByFilter(jobAdDtos);
    }

    @Override
    public FilterResponse<JobAdOutsideDetailResponse> filterFeaturedOutside(FilterRequest request) {
        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();
        long offset = request.getPageable().getOffset();
        long totalElements = 0L;
        int totalPages = 0;

        List<JobAdProjection> jobAdProjections = jobAdRepository.getFeaturedJobAds(pageSize, offset);
        List<JobAdDto> jobAdDtos = ObjectMapperUtils.convertToList(jobAdProjections, JobAdDto.class);
        if(!jobAdProjections.isEmpty()){
            totalElements = jobAdProjections.get(0).getTotalElement();
        }
        totalPages = (int) Math.ceil((double) totalElements / pageSize);

        List<JobAdOutsideDetailResponse> dtos = this.buildJobAdOutsideByFilter(jobAdDtos);
        FilterResponse<JobAdOutsideDetailResponse> filterResponse = new FilterResponse<>();
        filterResponse.setData(dtos);
        filterResponse.setPageInfo(PageInfo.builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNextPage(pageIndex + 1 < totalPages)
                .build());

        return filterResponse;
    }

    @Override
    public FilterResponse<JobAdOutsideDetailResponse> filterSuitableOutside(FilterRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();
        long offset = request.getPageable().getOffset();
        long totalElements = 0L;
        int totalPages = 0;

        FilterResponse<JobAdOutsideDetailResponse> filterResponse = new FilterResponse<>();
        filterResponse.setData(new ArrayList<>());
        filterResponse.setPageInfo(PageInfo.builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNextPage(pageIndex + 1 < totalPages)
                .build());
        if(userId == null){
            return filterResponse;
        }

        // get search history
        List<SearchHistoryOutsideDto> searchHistory = searchHistoryOutsideService.getMySearchHistoryOutside(); // limit 5
        String keyword = searchHistory.stream()
                .limit(1)
                .map(SearchHistoryOutsideDto::getKeyword)
                .distinct()
                .reduce("", (a, b) -> a + " " + b).trim();
        if(keyword.isEmpty()){
            return filterResponse;
        }

        // get suitable job ads
        List<JobAdProjection> jobAdProjections = jobAdRepository.getSuitableJobAds(keyword, pageSize, offset);
        if(jobAdProjections.isEmpty()){
            return filterResponse;
        }

        // calculate total elements and total pages
        List<JobAdDto> jobAdDtos = ObjectMapperUtils.convertToList(jobAdProjections, JobAdDto.class);
        if(!jobAdProjections.isEmpty()){
            totalElements = jobAdProjections.get(0).getTotalElement();
        }
        totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // build response
        List<JobAdOutsideDetailResponse> dtos = this.buildJobAdOutsideByFilter(jobAdDtos);

        filterResponse.setData(dtos);
        filterResponse.setPageInfo(PageInfo.builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNextPage(pageIndex + 1 < totalPages)
                .build());

        return filterResponse;
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

    private String convertDueDateToString(Instant dueDate) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate due = dueDate.atZone(ZoneId.systemDefault()).toLocalDate();

        long daysDiff = ChronoUnit.DAYS.between(today, due);

        if (daysDiff > 1) {
            return "Còn " + daysDiff + " ngày";
        } else if (daysDiff == 1) {
            return "Còn 1 ngày";
        } else if (daysDiff == 0) {
            return "Hôm nay hết hạn";
        } else {
            return "Đã hết hạn";
        }
    }

    private String convertSalaryToString(Integer salaryFrom, Integer salaryTo) {
        String fromStr = convertOneSalary(salaryFrom);
        String toStr = convertOneSalary(salaryTo);
        if (salaryFrom != null && salaryTo != null) {
            return fromStr + " - " + toStr + " triệu";
        }
        return fromStr;
    }

    private String convertOneSalary(Integer salary) {
        double million = salary / 1_000_000.0;
        if (million == (long) million) {
            return String.format("%d", (long) million);
        }
        return String.format("%.1f", million);
    }

    private List<String> getTags(JobAdDto jobAd, Long limitTag) {
        List<String> tags = new ArrayList<>();

        JobType jobType = JobType.getJobType(jobAd.getJobType());
        tags.add(jobType.getDescription());

        Long remain = (limitTag != null ? limitTag - 1 : null);

        if (jobAd.getIsRemote() != null && jobAd.getIsRemote()) {
            if (remain == null || remain > 0) {
                tags.add("Remote");
                if (remain != null) remain--;
            }
        }

        if (!ObjectUtils.isEmpty(jobAd.getKeyword())) {
            String[] keywords = jobAd.getKeyword().split(";");
            for (String k : keywords) {
                if (k == null || k.isBlank()) continue;
                if (remain != null && remain <= 0) break;
                tags.add(k.trim());
                if (remain != null) remain--;
            }
        }
        return tags;
    }

    private List<JobAdOutsideDetailResponse> buildJobAdOutsideByFilter(List<JobAdDto> jobAdDtos) {
        List<Long> positionIds = jobAdDtos.stream()
                .map(JobAdDto::getPositionId)
                .distinct()
                .toList();
        Map<Long, PositionDto> positionMap = positionService.getPositionMapByIds(positionIds);

        List<Long> jobAdIds = jobAdDtos.stream()
                .map(JobAdDto::getId)
                .toList();
        Map<Long, List<OrgAddressDto>> jobAdWorkLocationMap = orgAddressService.getOrgAddressByJobAdIds(jobAdIds);
        Map<Long, List<LevelDto>> jobAdLevelMap = levelService.getLevelsMapByJobAdIds(jobAdIds);

        List<Long> orgIds = jobAdDtos.stream()
                .map(JobAdDto::getOrgId)
                .distinct()
                .toList();
        Map<Long, OrgDto> orgMap = orgService.getOrgMapByIds(orgIds);

        return jobAdDtos.stream()
                .map(jobAd -> {
                    JobAdOutsideDetailResponse dto = new JobAdOutsideDetailResponse();
                    dto.setId(jobAd.getId());
                    dto.setTitle(jobAd.getTitle());
                    dto.setPosition(positionMap.get(jobAd.getPositionId()));
                    dto.setDueDate(jobAd.getDueDate());
                    dto.setDueDateStr(this.convertDueDateToString(jobAd.getDueDate()));
                    dto.setQuantity(jobAd.getQuantity());

                    if(jobAd.getSalaryFrom() != null && jobAd.getSalaryTo() != null){
                        dto.setSalaryStr(this.convertSalaryToString(jobAd.getSalaryFrom(), jobAd.getSalaryTo()));
                    } else {
                        SalaryType salaryType = SalaryType.valueOf(jobAd.getSalaryType());
                        dto.setSalaryStr(salaryType.getDescription());
                    }

                    dto.setIsRemote(jobAd.getIsRemote());
                    dto.setWorkLocations(jobAdWorkLocationMap.get(jobAd.getId()));
                    dto.setIsAllLevel(jobAd.getIsAllLevel());
                    if(jobAd.getIsAllLevel() == null || !jobAd.getIsAllLevel()) {
                        dto.setLevels(jobAdLevelMap.get(jobAd.getId()));
                    }

                    dto.setOrg(orgMap.get(jobAd.getOrgId()));

                    dto.setCreatedAt(jobAd.getCreatedAt());
                    dto.setKeyword(jobAd.getKeyword());
                    dto.setTags(this.getTags(jobAd, 4L));

                    return dto;
                })
                .toList();
    }

    private JobAdOutsideDetailResponse buildJobAdOutsideDetail(JobAdDto jobAd) {
        Long jobAdId = jobAd.getId();
        JobAdOutsideDetailResponse response = new JobAdOutsideDetailResponse();
        response.setTitle(jobAd.getTitle());
        response.setDueDate(jobAd.getDueDate());
        response.setDueDateStr(this.convertDueDateToString(jobAd.getDueDate()));
        response.setQuantity(jobAd.getQuantity());
        if(jobAd.getSalaryFrom() != null && jobAd.getSalaryTo() != null){
            response.setSalaryStr(this.convertSalaryToString(jobAd.getSalaryFrom(), jobAd.getSalaryTo()));
        } else {
            SalaryType salaryType = SalaryType.valueOf(jobAd.getSalaryType());
            response.setSalaryStr(salaryType.getDescription());
        }
        response.setDescription(jobAd.getDescription());
        response.setRequirement(jobAd.getRequirement());
        response.setBenefit(jobAd.getBenefit());
        response.setIsAllLevel(jobAd.getIsAllLevel());
        response.setTags(this.getTags(jobAd, null));

        Map<Long, PositionDto> positionMap = positionService.getPositionMapByIds(List.of(jobAd.getPositionId()));
        Map<Long, List<OrgAddressDto>> jobAdWorkLocationMap = orgAddressService.getOrgAddressByJobAdIds(List.of(jobAdId));
        Map<Long, List<LevelDto>> jobAdLevelMap = levelService.getLevelsMapByJobAdIds(List.of(jobAdId));
        OrgDto org = orgService.getOrgInfoOutside(jobAd.getOrgId());
        response.setPosition(positionMap.get(jobAd.getPositionId()));
        response.setWorkLocations(jobAdWorkLocationMap.get(jobAdId));
        response.setLevels(jobAdLevelMap.get(jobAdId));
        response.setOrg(org);
        return response;
    }
}
