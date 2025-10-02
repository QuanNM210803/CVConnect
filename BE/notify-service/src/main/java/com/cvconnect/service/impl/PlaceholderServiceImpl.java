package com.cvconnect.service.impl;

import com.cvconnect.dto.PlaceholderDto;
import com.cvconnect.entity.Placeholder;
import com.cvconnect.repository.PlaceholderRepository;
import com.cvconnect.service.PlaceholderService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceholderServiceImpl implements PlaceholderService {
    @Autowired
    private PlaceholderRepository placeholderRepository;

    @Override
    public List<PlaceholderDto> getByIds(List<Long> ids) {
        List<Placeholder> placeholders = placeholderRepository.findAllById(ids);
        if (placeholders.isEmpty()) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(placeholders, PlaceholderDto.class);
    }

    @Override
    public List<PlaceholderDto> filter() {
        List<Placeholder> placeholders = placeholderRepository.findAll();
        if(placeholders.isEmpty()){
            return List.of();
        }
        return ObjectMapperUtils.convertToList(placeholders, PlaceholderDto.class);
    }

    @Override
    public List<PlaceholderDto> getByEmailTemplateId(Long emailTemplateId) {
        List<Placeholder> placeholders = placeholderRepository.findByEmailTemplateId(emailTemplateId);
        if (placeholders.isEmpty()) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(placeholders, PlaceholderDto.class);
    }
}
