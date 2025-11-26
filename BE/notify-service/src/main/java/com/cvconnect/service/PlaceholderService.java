package com.cvconnect.service;

import com.cvconnect.dto.PlaceholderDto;

import java.util.List;

public interface PlaceholderService {
    List<PlaceholderDto> getByIds(List<Long> ids);
    List<PlaceholderDto> filter();
    List<PlaceholderDto> getByEmailTemplateId(Long emailTemplateId);
}
