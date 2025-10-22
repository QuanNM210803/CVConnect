package com.cvconnect.service.impl;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import com.cvconnect.dto.career.CareerRequest;
import com.cvconnect.entity.Careers;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.CareerRepository;
import com.cvconnect.service.CareerService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CareerServiceImpl implements CareerService {
    @Autowired
    private CareerRepository careerRepository;

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

    @Override
    public CareerDto getCareerDetail(Long careerId) {
        Careers career = careerRepository.findById(careerId).orElseThrow(
                () -> new AppException(CoreErrorCode.CAREER_NOT_FOUND)
        );
        return ObjectMapperUtils.convertToObject(career, CareerDto.class);
    }

    @Override
    @Transactional
    public IDResponse<Long> create(CareerRequest request) {
        List<Careers> careers = careerRepository.findAllByCodeIn(Collections.singletonList(request.getCode()));
        if (!careers.isEmpty()) {
            throw new AppException(CoreErrorCode.CAREER_CODE_EXISTS, request.getCode());
        }
        Careers entity = new Careers();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        careerRepository.save(entity);
        return IDResponse.<Long>builder()
                .id(entity.getId())
                .build();
    }

    @Override
    @Transactional
    public IDResponse<Long> update(CareerRequest request) {
        Careers existingCareer = careerRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(CoreErrorCode.CAREER_NOT_FOUND)
        );
        List<Careers> careers = careerRepository.findAllByCodeIn(Collections.singletonList(request.getCode()));
        if (!careers.isEmpty()) {
            for (Careers career : careers) {
                if (!career.getId().equals(request.getId())) {
                    throw new AppException(CoreErrorCode.CAREER_CODE_EXISTS, request.getCode());
                }
            }
        }
        existingCareer.setCode(request.getCode());
        existingCareer.setName(request.getName());
        careerRepository.save(existingCareer);
        return IDResponse.<Long>builder()
                .id(existingCareer.getId())
                .build();
    }

}
