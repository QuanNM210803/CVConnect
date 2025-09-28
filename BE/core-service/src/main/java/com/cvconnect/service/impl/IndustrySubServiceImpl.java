package com.cvconnect.service.impl;

import com.cvconnect.dto.IndustrySubDto;
import com.cvconnect.entity.IndustrySub;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.IndustrySubRepository;
import com.cvconnect.service.IndustrySubService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndustrySubServiceImpl implements IndustrySubService {
    @Autowired
    private IndustrySubRepository industrySubRepository;

    @Override
    public Map<Long, List<IndustrySubDto>> getIndustryIds(List<Long> industryIds) {
        if(Objects.isNull(industryIds) || industryIds.isEmpty()){
            return Map.of();
        }
        List<IndustrySub> entities = industrySubRepository.findByIndustryIds(industryIds);
        if(entities.isEmpty()){
            return Map.of();
        }
        return ObjectMapperUtils.convertToList(entities, IndustrySubDto.class).stream()
                .collect(Collectors.groupingBy(IndustrySubDto::getIndustryId));
    }

    @Override
    @Transactional
    public void create(List<IndustrySubDto> industrySubDtos) {
        Map<String, Long> freq = industrySubDtos.stream()
                .collect(Collectors.groupingBy(IndustrySubDto::getCode, Collectors.counting()));
        List<String> duplicatedCodes = freq.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
        if (!duplicatedCodes.isEmpty()) {
            throw new AppException(CoreErrorCode.INDUSTRY_SUB_CODE_DUPLICATED, String.join(", ", duplicatedCodes));
        }

        Map<String, Long> inputCodeToId = new LinkedHashMap<>();
        industrySubDtos.forEach(dto -> inputCodeToId.putIfAbsent(dto.getCode(), dto.getId()));
        Set<String> inputCodes = inputCodeToId.keySet();
        List<IndustrySub> existingEntities = industrySubRepository.findAllByCodeIn(inputCodes);
        List<String> conflictCodes = existingEntities.stream()
                .filter(entity -> {
                    Long inputId = inputCodeToId.get(entity.getCode());
                    return Objects.isNull(inputId) || !entity.getId().equals(inputId);
                })
                .map(IndustrySub::getCode)
                .toList();
        if (!conflictCodes.isEmpty()) {
            throw new AppException(CoreErrorCode.INDUSTRY_SUB_CODE_EXISTS, String.join(", ", conflictCodes));
        }

        List<IndustrySub> entities = ObjectMapperUtils.convertToList(industrySubDtos, IndustrySub.class);
        industrySubRepository.saveAll(entities);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if(Objects.nonNull(ids) && !ids.isEmpty()){
            industrySubRepository.deleteAllById(ids);
        }
    }

}
