package com.cvconnect.service.impl;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.entity.Industry;
import com.cvconnect.repository.IndustryRepository;
import com.cvconnect.service.IndustryService;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

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
            request.setCreatedAtEnd(request.getCreatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(request.getUpdatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        Page<Industry> page = industryRepository.filter(request, request.getPageable());
        List<IndustryDto> data = ObjectMapperUtils.convertToList(page.getContent(), IndustryDto.class);
        return PageUtils.toFilterResponse(page, data);
    }

    @Override
    public FilterResponse<IndustryDto> filterPublic(IndustryFilterRequest request) {
        FilterResponse<IndustryDto> response = this.filter(request);
        return CoreServiceUtils.configResponsePublic(response);
    }
}
