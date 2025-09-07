package com.cvconnect.service.impl;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.entity.Industry;
import com.cvconnect.repository.IndustryRepository;
import com.cvconnect.service.IndustryService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
