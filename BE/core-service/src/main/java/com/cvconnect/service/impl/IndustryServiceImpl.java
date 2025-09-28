package com.cvconnect.service.impl;

import com.cvconnect.dto.IndustrySubDto;
import com.cvconnect.dto.IndustrySubRequest;
import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.dto.industry.IndustryRequest;
import com.cvconnect.entity.Industry;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.IndustryRepository;
import com.cvconnect.service.IndustryService;
import com.cvconnect.service.IndustrySubService;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private IndustrySubService industrySubService;

    @Override
    public List<IndustryDto> findByIds(List<Long> ids) {
        List<Industry> entities = industryRepository.findAllById(ids);
        return entities.stream()
                .map(entity -> ObjectMapperUtils.convertToObject(entity, IndustryDto.class))
                .toList();
    }

    @Override
    public FilterResponse<IndustryDto> filter(IndustryFilterRequest request) {
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(request.getCreatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(request.getUpdatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        Page<Industry> page = industryRepository.filter(request, request.getPageable());
        List<IndustryDto> data = ObjectMapperUtils.convertToList(page.getContent(), IndustryDto.class);

        // get industrySub
        List<Long> industryIds = data.stream()
                .map(IndustryDto::getId)
                .toList();
        Map<Long, List<IndustrySubDto>> industrySubMap = industrySubService.getIndustryIds(industryIds);
        data.forEach(industryDto -> {
            industryDto.setIndustrySubs(industrySubMap.get(industryDto.getId()));
        });

        return PageUtils.toFilterResponse(page, data);
    }

    @Override
    public FilterResponse<IndustryDto> filterPublic(IndustryFilterRequest request) {
        FilterResponse<IndustryDto> response = this.filter(request);
        response.getData().forEach(industryDto -> {
            industryDto.setIndustrySubs(null);
        });
        return CoreServiceUtils.configResponsePublic(response);
    }

    @Override
    public IndustryDto detail(Long id) {
        Industry entity = industryRepository.findById(id).orElse(null);
        if (Objects.isNull(entity)) {
            return null;
        }
        IndustryDto dto = ObjectMapperUtils.convertToObject(entity, IndustryDto.class);
        List<IndustrySubDto> industrySubs = industrySubService.getIndustryIds(List.of(id)).get(id);
        dto.setIndustrySubs(industrySubs);
        return dto;
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        List<Industry> entities = industryRepository.findAllById(ids);
        if(entities.size() != ids.size()) {
            throw new AppException(CoreErrorCode.INDUSTRY_NOT_FOUND);
        }
        industryRepository.deleteAll(entities);
    }

    @Override
    @Transactional
    public IDResponse<Long> create(IndustryRequest request) {
        boolean exists = industryRepository.existsByCode(request.getCode());
        if (exists) {
            throw new AppException(CoreErrorCode.INDUSTRY_CODE_ALREADY_EXISTS, request.getCode());
        }
        Industry entity = new Industry();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        industryRepository.save(entity);

        this.createIndustrySubs(request.getIndustrySubs(), entity.getId());
        return IDResponse.<Long>builder()
                .id(entity.getId())
                .build();
    }

    @Override
    @Transactional
    public IDResponse<Long> update(IndustryRequest request) {
        Industry entity = industryRepository.findById(request.getId()).orElse(null);
        if (Objects.isNull(entity)) {
            throw new AppException(CoreErrorCode.INDUSTRY_NOT_FOUND);
        }
        Industry existsByCode = industryRepository.findByCode(request.getCode());
        if (Objects.nonNull(existsByCode) && !Objects.equals(entity.getId(), existsByCode.getId())) {
            throw new AppException(CoreErrorCode.INDUSTRY_CODE_ALREADY_EXISTS, request.getCode());
        }
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        industryRepository.save(entity);

        // save industrySub
        Long industryId = entity.getId();
        List<IndustrySubRequest> industrySubInRequest = request.getIndustrySubs();
        List<IndustrySubDto> industrySubInDB = industrySubService.getIndustryIds(List.of(industryId)).get(industryId);
        List<Long> deleteIds = CoreServiceUtils.getDeleteIds(industrySubInRequest, industrySubInDB);
        industrySubService.deleteByIds(deleteIds);
        this.createIndustrySubs(industrySubInRequest, industryId);

        return IDResponse.<Long>builder()
                .id(industryId)
                .build();
    }

    void createIndustrySubs(List<IndustrySubRequest> listSub, Long industryId) {
        if (Objects.nonNull(listSub) && !listSub.isEmpty()) {
            List<IndustrySubDto> industrySubDtos = ObjectMapperUtils.convertToList(listSub, IndustrySubDto.class);
            industrySubDtos.forEach(subDto -> subDto.setIndustryId(industryId));
            industrySubService.create(industrySubDtos);
        }
    }
}
