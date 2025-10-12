package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.dto.EmailConfigRequest;
import com.cvconnect.entity.EmailConfig;
import com.cvconnect.enums.NotifyErrorCode;
import com.cvconnect.repository.EmailConfigRepository;
import com.cvconnect.service.EmailConfigService;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConfigServiceImpl implements EmailConfigService {
    @Autowired
    private EmailConfigRepository emailConfigRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

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

    @Override
    public EmailConfigDto detail() {
        Long orgId = restTemplateClient.validOrgMember();
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

    @Override
    public IDResponse<Long> create(EmailConfigRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        boolean exists = emailConfigRepository.existsByOrgId(orgId);
        if (exists) {
            throw new AppException(NotifyErrorCode.EMAIL_CONFIG_NOT_FOUND);
        }
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setOrgId(orgId);
        emailConfig.setHost(request.getHost());
        emailConfig.setPort(request.getPort());
        emailConfig.setEmail(request.getEmail());
        emailConfig.setPassword(request.getPassword());
        emailConfig.setIsSsl(request.getIsSsl());
        emailConfig.setProtocol(request.getProtocol());
        emailConfigRepository.save(emailConfig);
        return IDResponse.<Long>builder()
                .id(emailConfig.getId())
                .build();
    }

    @Override
    public IDResponse<Long> update(EmailConfigRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        EmailConfig emailConfig = emailConfigRepository.findByIdAndOrgId(request.getId(), orgId);
        if (emailConfig == null) {
            throw new AppException(NotifyErrorCode.EMAIL_CONFIG_NOT_FOUND);
        }
        emailConfig.setHost(request.getHost());
        emailConfig.setPort(request.getPort());
        emailConfig.setEmail(request.getEmail());
        emailConfig.setPassword(request.getPassword());
        emailConfig.setIsSsl(request.getIsSsl());
        emailConfig.setProtocol(request.getProtocol());
        emailConfigRepository.save(emailConfig);
        return IDResponse.<Long>builder()
                .id(emailConfig.getId())
                .build();
    }

    @Override
    public void delete(Long id) {
        Long orgId = restTemplateClient.validOrgMember();
        EmailConfig emailConfig = emailConfigRepository.findByIdAndOrgId(id, orgId);
        if (emailConfig == null) {
            throw new AppException(NotifyErrorCode.EMAIL_CONFIG_NOT_FOUND);
        }
        emailConfigRepository.delete(emailConfig);
    }
}
