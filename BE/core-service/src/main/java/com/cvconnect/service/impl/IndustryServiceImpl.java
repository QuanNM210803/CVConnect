package com.cvconnect.service.impl;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.dto.industry.IndustryRequest;
import com.cvconnect.entity.Industry;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.IndustryRepository;
import com.cvconnect.service.IndustryService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Autowired
    private IndustryRepository industryRepository;

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
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<Industry> page = industryRepository.filter(request, request.getPageable());
        List<IndustryDto> data = ObjectMapperUtils.convertToList(page.getContent(), IndustryDto.class);

        return PageUtils.toFilterResponse(page, data);
    }

    @Override
    public FilterResponse<IndustryDto> filterPublic(IndustryFilterRequest request) {
        FilterResponse<IndustryDto> response = this.filter(request);
        return WebUtils.configResponsePublic(response);
    }

    @Override
    public IndustryDto detail(Long id) {
        Industry entity = industryRepository.findById(id).orElse(null);
        if (Objects.isNull(entity)) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(entity, IndustryDto.class);
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

        return IDResponse.<Long>builder()
                .id(entity.getId())
                .build();
    }

    @Override
    public List<IndustryDto> getIndustriesByOrgId(Long orgId) {
        List<Industry> entities = industryRepository.getIndustriesByOrgId(orgId);
        return ObjectMapperUtils.convertToList(entities, IndustryDto.class);
    }
}
