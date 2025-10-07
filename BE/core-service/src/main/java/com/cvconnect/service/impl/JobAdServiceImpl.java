package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.jobAd.*;
import com.cvconnect.dto.positionProcess.PositionProcessRequest;
import com.cvconnect.dto.internal.response.EmailTemplateDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.JobAd;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.enums.JobAdStatus;
import com.cvconnect.enums.ProcessTypeEnum;
import com.cvconnect.enums.SalaryType;
import com.cvconnect.repository.JobAdRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class JobAdServiceImpl implements JobAdService {
    @Autowired
    private JobAdRepository jobAdRepository;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private JobAdIndustrySubService jobAdIndustrySubService;
    @Autowired
    private JobAdWorkLocationService jobAdWorkLocationService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private OrgAddressService orgAddressService;
    @Autowired
    private RestTemplateClient restTemplateClient;

    private static final String JOB_AD_CODE_PREFIX = "JD-";

    @Override
    @Transactional
    public IDResponse<Long> create(JobAdRequest request) {
        Long orgId = WebUtils.checkCurrentOrgId();
        request.setOrgId(orgId);
        this.validateCreate(request);

        Long suffixMax = jobAdRepository.getSuffixCodeMax(request.getOrgId(), JOB_AD_CODE_PREFIX);
        suffixMax = suffixMax == null ? 0 : suffixMax;

        JobAd jobAd = new JobAd();
        jobAd.setCode(JOB_AD_CODE_PREFIX + (suffixMax + 1));
        jobAd.setTitle(request.getTitle());
        jobAd.setOrgId(request.getOrgId());
        jobAd.setPositionId(request.getPositionId());
        jobAd.setPositionLevelId(request.getPositionLevelId());
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
        jobAdRepository.save(jobAd);

        if(!ObjectUtils.isEmpty(request.getIndustrySubIds())){
            List<JobAdIndustrySubDto> jobAdIndustrySubDtos = request.getIndustrySubIds().stream()
                    .map(industrySubId -> JobAdIndustrySubDto.builder()
                            .jobAdId(jobAd.getId())
                            .industrySubId(industrySubId)
                            .build())
                    .collect(Collectors.toList());
            jobAdIndustrySubService.create(jobAdIndustrySubDtos);
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

    private void validateCreate(JobAdRequest request) {
        // validate orgId, positionId, positionLevelId
        boolean exists = jobAdRepository.existsByOrgIdAndPositionIdAndPositionLevelId(
                request.getOrgId(), request.getPositionId(), request.getPositionLevelId());
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
        if(!JobAdStatus.DRAFT.equals(request.getJobAdStatus()) && !JobAdStatus.OPEN.equals(request.getJobAdStatus())){
            throw new AppException(CoreErrorCode.JOB_AD_STATUS_INVALID);
        }

        // validate emailTemplateId if isAutoSendEmail = true
        if(request.isAutoSendEmail()){
            if(request.getEmailTemplateId() == null){
                throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_ID_REQUIRED);
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
    }
}
