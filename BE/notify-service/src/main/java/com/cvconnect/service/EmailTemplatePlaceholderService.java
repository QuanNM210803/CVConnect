package com.cvconnect.service;

import com.cvconnect.dto.EmailTemplatePlaceholderDto;

import java.util.List;

public interface EmailTemplatePlaceholderService {
    void create(List<EmailTemplatePlaceholderDto> placeholders);
    void deleteByEmailTemplateIdAndPlaceholderIds(Long emailTemplateId, List<Long> placeholderIds);
}
