package com.cvconnect.service.impl;

import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.entity.EmailConfig;
import com.cvconnect.repository.EmailConfigRepository;
import com.cvconnect.service.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConfigServiceImpl implements EmailConfigService {
    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Override
    public EmailConfigDto getByOrgId(Long orgId) {
        EmailConfig emailConfig = emailConfigRepository.findByOrgId(orgId);
        if (emailConfig == null) {
            return null;
        }
        return EmailConfigDto.builder()
                .host(emailConfig.getHost())
                .port(emailConfig.getPort())
                .email(emailConfig.getEmail())
                .password(emailConfig.getPassword())
                .isSsl(emailConfig.getIsSsl())
                .protocol(emailConfig.getProtocol())
                .build();
    }
}
