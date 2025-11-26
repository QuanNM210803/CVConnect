package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.attachFile.AttachFileDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyProjection;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoFilterByJobAdProcess;
import com.cvconnect.dto.candidateSummaryOrg.CandidateSummaryOrgDto;
import com.cvconnect.dto.enums.CandidateStatusDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.entity.CandidateInfoApply;
import com.cvconnect.enums.CandidateStatus;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.CandidateInfoApplyRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CandidateInfoApplyServiceImpl implements CandidateInfoApplyService {
    @Autowired
    private CandidateInfoApplyRepository candidateInfoApplyRepository;
    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private JobAdProcessService jobAdProcessService;
    @Autowired
    private JobAdService jobAdService;
    @Autowired
    private InterviewPanelService interviewPanelService;

    @Override
    public FilterResponse<CandidateInfoApplyDto> filter(CandidateInfoApplyFilterRequest request) {
        Long candidateId = WebUtils.getCurrentUserId();
        request.setCandidateId(candidateId);
        Page<CandidateInfoApply> page = candidateInfoApplyRepository.filter(request, request.getPageable());
        List<CandidateInfoApplyDto> dtos = ObjectMapperUtils.convertToList(page.getContent(), CandidateInfoApplyDto.class);

        // get attach file CV
        List<Long> attachFileIds = dtos.stream()
                .map(CandidateInfoApplyDto::getCvFileId)
                .toList();
        List<AttachFileDto> attachFileDtos = attachFileService.getAttachFiles(attachFileIds);
        Map<Long, AttachFileDto> attachFileMap = attachFileDtos.stream()
                .collect(Collectors.toMap(AttachFileDto::getId, Function.identity()));
        dtos.forEach(dto -> dto.setAttachFile(attachFileMap.get(dto.getCvFileId())));
        return PageUtils.toFilterResponse(page, dtos);
    }

    @Override
    public CandidateInfoApplyDto getById(Long candidateInfoApplyId) {
        CandidateInfoApply candidateInfoApply = candidateInfoApplyRepository.findById(candidateInfoApplyId).orElse(null);
        if(ObjectUtils.isEmpty(candidateInfoApply)) {
            return null;
        }
        CandidateInfoApplyDto dto = ObjectMapperUtils.convertToObject(candidateInfoApply, CandidateInfoApplyDto.class);
        List<AttachFileDto> attachFileDtos = attachFileService.getAttachFiles(List.of(dto.getCvFileId()));
        if(!attachFileDtos.isEmpty()) {
            dto.setAttachFile(attachFileDtos.get(0));
        }
        return dto;
    }

    @Override
    public List<Long> create(List<CandidateInfoApplyDto> dtos) {
        List<CandidateInfoApply> entities = ObjectMapperUtils.convertToList(dtos, CandidateInfoApply.class);
        candidateInfoApplyRepository.saveAll(entities);
        return entities.stream()
                .map(CandidateInfoApply::getId)
                .toList();
    }

    @Override
    public Boolean validateCandidateInfoInProcess(List<Long> candidateInfoIds, Long jobAdProcessId) {
        Long count = candidateInfoApplyRepository.countByCandidateInfoIdsAndJobAdProcessId(candidateInfoIds, jobAdProcessId);
        return count != null && count.equals((long) candidateInfoIds.size());
    }

    @Override
    public Map<Long, CandidateInfoApplyDto> getByIds(List<Long> candidateInfoIds) {
        List<CandidateInfoApply> entities = candidateInfoApplyRepository.findAllById(candidateInfoIds);
        if(ObjectUtils.isEmpty(entities)) {
            return Map.of();
        }
        List<CandidateInfoApplyDto> dtos = ObjectMapperUtils.convertToList(entities, CandidateInfoApplyDto.class);
        return dtos.stream()
                .collect(Collectors.toMap(CandidateInfoApplyDto::getId, Function.identity()));
    }

    @Override
    public List<CandidateInfoApplyDto> getByCalendarId(Long calendarId) {
        List<CandidateInfoApply> entities = candidateInfoApplyRepository.findByCalendarId(calendarId);
        if(ObjectUtils.isEmpty(entities)) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(entities, CandidateInfoApplyDto.class);
    }

    @Override
    public List<CandidateInfoApplyDto> getCandidateInCurrentProcess(Long jobAdProcessId) {
        Long orgId = restTemplateClient.validOrgMember();
        Boolean exists = jobAdProcessService.existByJobAdProcessIdAndOrgId(jobAdProcessId, orgId);
        if(!exists) {
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        List<CandidateInfoApply> entities = candidateInfoApplyRepository.findCandidateInCurrentProcess(jobAdProcessId);
        List<CandidateInfoApplyDto> dtos = ObjectMapperUtils.convertToList(entities, CandidateInfoApplyDto.class);

        if(ObjectUtils.isEmpty(dtos)) {
            return List.of();
        }

        List<Long> candidateInfoIds = dtos.stream()
                .map(CandidateInfoApplyDto::getId)
                .toList();
        List<Long> candidateInfoHasSchedule = candidateInfoApplyRepository.getCandidateInfoHasSchedule(jobAdProcessId, candidateInfoIds);
        for (CandidateInfoApplyDto dto : dtos) {
            dto.setHasSchedule(candidateInfoHasSchedule.contains(dto.getId()));
        }
        return dtos;
    }

    @Override
    public FilterResponse<CandidateInfoApplyDto> filterByJobAdProcess(CandidateInfoFilterByJobAdProcess request) {
        Long orgId = restTemplateClient.validOrgMember();
        request.setOrgId(orgId);

        JobAdDto jobAdDto = jobAdService.findByJobAdProcessId(request.getJobAdProcessId());
        if(ObjectUtils.isEmpty(jobAdDto)) {
            throw new AppException(CoreErrorCode.JOB_AD_NOT_FOUND);
        }
        if(!jobAdDto.getOrgId().equals(orgId)) {
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }

        Long currentUserId = WebUtils.getCurrentUserId();
        List<String> roles = WebUtils.getCurrentRole();
        if(!roles.contains(Constants.RoleCode.ORG_ADMIN)) {
            Boolean hasInterviewPanel = interviewPanelService.existByJobAdIdAndUserId(jobAdDto.getId(), currentUserId);
            if(!currentUserId.equals(jobAdDto.getHrContactId()) && !hasInterviewPanel) {
                throw new AppException(CommonErrorCode.ACCESS_DENIED);
            }
        }

        Pageable pageable = request.getPageable();
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            boolean hasCreatedAt = sort.stream().anyMatch(order -> order.getProperty().equalsIgnoreCase(CommonConstants.DEFAULT_SORT_BY));
            if (hasCreatedAt) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "applyDate"));
            }
        }

        Page<CandidateInfoApplyProjection> page = candidateInfoApplyRepository.filterByJobAdProcess(request, pageable);
        List<CandidateInfoApplyDto> dtos = page.getContent().stream()
                .map(projection -> {
                    CandidateInfoApplyDto dto = new CandidateInfoApplyDto();
                    dto.setId(projection.getId());
                    dto.setFullName(projection.getFullName());
                    dto.setEmail(projection.getEmail());
                    dto.setPhone(projection.getPhone());

                    CandidateSummaryOrgDto candidateSummaryOrgDto = new CandidateSummaryOrgDto();
                    LevelDto levelDto = new LevelDto();
                    levelDto.setId(projection.getLevelId());
                    levelDto.setLevelName(projection.getLevelName());
                    candidateSummaryOrgDto.setLevel(levelDto);
                    dto.setCandidateSummaryOrg(candidateSummaryOrgDto);

                    CandidateStatusDto candidateStatusDto = CandidateStatus.getCandidateStatusDto(projection.getCandidateStatus());
                    dto.setCandidateStatus(candidateStatusDto);

                    dto.setApplyDate(projection.getApplyDate());
                    dto.setOnboardDate(projection.getOnboardDate());

                    return dto;
                }).toList();

        return PageUtils.toFilterResponse(page, dtos);
    }
}
