package com.cvconnect.service.impl;

import com.cvconnect.dto.EmailTemplatePlaceholderDto;
import com.cvconnect.entity.EmailTemplatePlaceholder;
import com.cvconnect.repository.EmailTemplatePlaceholderRepository;
import com.cvconnect.service.EmailTemplatePlaceholderService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTemplatePlaceholderServiceImpl implements EmailTemplatePlaceholderService {
    @Autowired
    private EmailTemplatePlaceholderRepository emailTemplatePlaceholderRepository;

    @Override
    public void create(List<EmailTemplatePlaceholderDto> placeholders) {
        List<EmailTemplatePlaceholder> entities = ObjectMapperUtils.convertToList(placeholders, EmailTemplatePlaceholder.class);
        emailTemplatePlaceholderRepository.saveAll(entities);
    }

    @Override
    public void deleteByEmailTemplateIdAndPlaceholderIds(Long emailTemplateId, List<Long> placeholderIds) {
        emailTemplatePlaceholderRepository.deleteByEmailTemplateIdAndPlaceholderIdIn(emailTemplateId, placeholderIds);
    }
}
