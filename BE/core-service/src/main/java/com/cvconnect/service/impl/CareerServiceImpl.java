package com.cvconnect.service.impl;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import com.cvconnect.entity.Careers;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.CareerRepository;
import com.cvconnect.service.CareerService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CareerServiceImpl implements CareerService {
    @Autowired
    private CareerRepository careerRepository;

    @Override
    @Transactional
    public void create(List<CareerDto> careerDtos) {
        Map<String, Long> freq = careerDtos.stream()
                .collect(Collectors.groupingBy(CareerDto::getCode, Collectors.counting()));
        List<String> duplicatedCodes = freq.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
        if (!duplicatedCodes.isEmpty()) {
            throw new AppException(CoreErrorCode.CAREER_CODE_DUPLICATED, String.join(", ", duplicatedCodes));
        }

        Map<String, Long> inputCodeToId = new LinkedHashMap<>();
        careerDtos.forEach(dto -> inputCodeToId.putIfAbsent(dto.getCode(), dto.getId()));
        Set<String> inputCodes = inputCodeToId.keySet();
        List<Careers> existingEntities = careerRepository.findAllByCodeIn(inputCodes);
        List<String> conflictCodes = existingEntities.stream()
                .filter(entity -> {
                    Long inputId = inputCodeToId.get(entity.getCode());
                    return Objects.isNull(inputId) || !entity.getId().equals(inputId);
                })
                .map(Careers::getCode)
                .toList();
        if (!conflictCodes.isEmpty()) {
            throw new AppException(CoreErrorCode.CAREER_CODE_EXISTS, String.join(", ", conflictCodes));
        }

        List<Careers> entities = ObjectMapperUtils.convertToList(careerDtos, Careers.class);
        careerRepository.saveAll(entities);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if(Objects.nonNull(ids) && !ids.isEmpty()){
            careerRepository.deleteAllById(ids);
        }
    }

    @Override
    public FilterResponse<CareerDto> filter(CareerFilterRequest request) {
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<Careers> page = careerRepository.filter(request, request.getPageable());
        List<CareerDto> dtos = ObjectMapperUtils.convertToList(page.getContent(), CareerDto.class);
        return PageUtils.toFilterResponse(page, dtos);
    }

}
