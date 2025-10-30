package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.attachFile.AttachFileDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyFilterRequest;
import com.cvconnect.entity.CandidateInfoApply;
import com.cvconnect.repository.CandidateInfoApplyRepository;
import com.cvconnect.service.AttachFileService;
import com.cvconnect.service.CandidateInfoApplyService;
import com.cvconnect.service.JobAdProcessService;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
}
