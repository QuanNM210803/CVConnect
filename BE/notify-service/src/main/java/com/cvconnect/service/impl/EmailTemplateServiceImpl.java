package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.*;
import com.cvconnect.dto.internal.request.DataReplacePlaceholder;
import com.cvconnect.entity.EmailTemplate;
import com.cvconnect.enums.NotifyErrorCode;
import com.cvconnect.repository.EmailTemplateRepository;
import com.cvconnect.service.EmailTemplatePlaceholderService;
import com.cvconnect.service.EmailTemplateService;
import com.cvconnect.service.PlaceholderService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
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
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private EmailTemplatePlaceholderService emailTemplatePlaceholderService;
    @Autowired
    private PlaceholderService placeholderService;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    @Transactional
    public IDResponse<Long> create(EmailTemplateRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        boolean exists = emailTemplateRepository.existsByCodeAndOrgId(request.getCode(), orgId);
        if (exists) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_CODE_EXISTED);
        }
        EmailTemplate emailTemplate = ObjectMapperUtils.convertToObject(request, EmailTemplate.class);
        emailTemplate.setOrgId(orgId);
        emailTemplateRepository.save(emailTemplate);

        if(!ObjectUtils.isEmpty(request.getPlaceholderIds())){
            List<PlaceholderDto> existingPlaceholders = placeholderService.getByIds(request.getPlaceholderIds());
            if (existingPlaceholders.size() != request.getPlaceholderIds().size()) {
                throw new AppException(NotifyErrorCode.PLACEHOLDER_NOT_FOUND);
            }
            List<EmailTemplatePlaceholderDto> placeholders = request.getPlaceholderIds().stream()
                    .map(id -> EmailTemplatePlaceholderDto.builder()
                            .emailTemplateId(emailTemplate.getId())
                            .placeholderId(id)
                            .build()
                    ).collect(Collectors.toList());
            emailTemplatePlaceholderService.create(placeholders);
        }

        return IDResponse.<Long>builder()
                .id(emailTemplate.getId())
                .build();
    }

    @Override
    public FilterResponse<EmailTemplateDto> filter(EmailTemplateFilterRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        request.setOrgId(orgId);
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<EmailTemplate> page = emailTemplateRepository.filter(request, request.getPageable());
        List<EmailTemplateDto> data = ObjectMapperUtils.convertToList(page.getContent(), EmailTemplateDto.class);
        return PageUtils.toFilterResponse(page, data);
    }

    @Override
    @Transactional
    public IDResponse<Long> update(EmailTemplateRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        EmailTemplate emailTemplate = emailTemplateRepository.findById(request.getId()).orElse(null);
        if (ObjectUtils.isEmpty(emailTemplate) || !emailTemplate.getOrgId().equals(orgId)) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
        boolean exists = emailTemplateRepository.existsByCodeAndOrgId(request.getCode(), orgId);
        if (exists && !emailTemplate.getCode().equals(request.getCode())) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_CODE_EXISTED);
        }
        emailTemplate.setCode(request.getCode());
        emailTemplate.setName(request.getName());
        emailTemplate.setSubject(request.getSubject());
        emailTemplate.setBody(request.getBody());
        emailTemplateRepository.save(emailTemplate);

        List<Long> placeholderIdsRequest = Optional.ofNullable(request.getPlaceholderIds()).orElse(Collections.emptyList());
        List<PlaceholderDto> placeholderDb = Optional.ofNullable(placeholderService.getByEmailTemplateId(emailTemplate.getId()))
                .orElse(Collections.emptyList());
        List<Long> placeholderIdsDb = placeholderDb.stream()
                .map(PlaceholderDto::getId)
                .toList();

        List<Long> toAdd = placeholderIdsRequest.stream()
                .filter(id -> !placeholderIdsDb.contains(id))
                .toList();
        List<Long> toRemove = placeholderIdsDb.stream()
                .filter(id -> !placeholderIdsRequest.contains(id))
                .toList();
        if (!ObjectUtils.isEmpty(toAdd)) {
            List<PlaceholderDto> existingPlaceholders = placeholderService.getByIds(toAdd);
            if (existingPlaceholders.size() != toAdd.size()) {
                throw new AppException(NotifyErrorCode.PLACEHOLDER_NOT_FOUND);
            }
            List<EmailTemplatePlaceholderDto> placeholders = toAdd.stream()
                    .map(id -> EmailTemplatePlaceholderDto.builder()
                            .emailTemplateId(emailTemplate.getId())
                            .placeholderId(id)
                            .build()
                    ).collect(Collectors.toList());
            emailTemplatePlaceholderService.create(placeholders);
        }
        if (!ObjectUtils.isEmpty(toRemove)) {
            emailTemplatePlaceholderService.deleteByEmailTemplateIdAndPlaceholderIds(emailTemplate.getId(), toRemove);
        }

        return IDResponse.<Long>builder()
                .id(emailTemplate.getId())
                .build();
    }

    @Override
    public EmailTemplateDto detail(Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND));
        EmailTemplateDto emailTemplateDto = ObjectMapperUtils.convertToObject(emailTemplate, EmailTemplateDto.class);

        List<PlaceholderDto> placeholders = placeholderService.getByEmailTemplateId(id);
        emailTemplateDto.setPlaceholders(placeholders);
        return emailTemplateDto;
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        Long orgId = restTemplateClient.validOrgMember();
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findByIdsAndOrgId(ids, orgId);
        if (emailTemplates.size() != ids.size()) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
        emailTemplateRepository.deleteAll(emailTemplates);
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findByIdsAndOrgId(request.getIds(), orgId);
        if (emailTemplates.size() != request.getIds().size()) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
        emailTemplates.forEach(emailTemplate -> emailTemplate.setIsActive(request.getActive()));
        emailTemplateRepository.saveAll(emailTemplates);
    }

    @Override
    public List<EmailTemplateDto> getByOrgId(Long orgId) {
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findByOrgIdAndIsActive(orgId, null);
        if (ObjectUtils.isEmpty(emailTemplates)) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(emailTemplates, EmailTemplateDto.class);
    }

    @Override
    public EmailTemplateDto getById(Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND));
        EmailTemplateDto emailTemplateDto = ObjectMapperUtils.convertToObject(emailTemplate, EmailTemplateDto.class);

        List<PlaceholderDto> placeholders = placeholderService.getByEmailTemplateId(id);
        List<String> placeholderCodes = placeholders.stream()
                .map(PlaceholderDto::getCode)
                .toList();
        emailTemplateDto.setPlaceholderCodes(placeholderCodes);
        return emailTemplateDto;
    }

    @Override
    public EmailTemplateDto previewEmail(Long id, DataReplacePlaceholder dataReplacePlaceholder) {
        Long orgId = restTemplateClient.validOrgMember();
        EmailTemplateDto emailTemplateDto = getById(id);
        if (!emailTemplateDto.getOrgId().equals(orgId)) {
            throw new AppException(NotifyErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
        Long userId = WebUtils.getCurrentUserId();
        String fullName = WebUtils.getCurrentFullName();
        String email = WebUtils.getCurrentEmail();
        if(dataReplacePlaceholder == null) {
            dataReplacePlaceholder = new DataReplacePlaceholder();
        }
        dataReplacePlaceholder.setHrContactId(userId);
        dataReplacePlaceholder.setHrName(fullName);
        dataReplacePlaceholder.setHrEmail(email);
        dataReplacePlaceholder.setHrPhone(null);
        String bodyPreview = restTemplateClient.previewEmail(emailTemplateDto.getBody(), emailTemplateDto.getPlaceholderCodes(), dataReplacePlaceholder, false);
        emailTemplateDto.setBodyPreview(bodyPreview);
        return emailTemplateDto;
    }

    @Override
    public EmailTemplateDto previewEmail(PreviewEmailWithoutTemplate request) {
        DataReplacePlaceholder dataReplacePlaceholder = request.getDataReplacePlaceholder();
        Long userId = WebUtils.getCurrentUserId();
        String fullName = WebUtils.getCurrentFullName();
        String email = WebUtils.getCurrentEmail();
        if(dataReplacePlaceholder == null) {
            dataReplacePlaceholder = new DataReplacePlaceholder();
        }
        dataReplacePlaceholder.setHrContactId(userId);
        dataReplacePlaceholder.setHrName(fullName);
        dataReplacePlaceholder.setHrEmail(email);
        dataReplacePlaceholder.setHrPhone(null);
        String bodyPreview = restTemplateClient.previewEmail(request.getBody(), request.getPlaceholderCodes(), dataReplacePlaceholder, false);
        EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
        emailTemplateDto.setSubject(request.getSubject());
        emailTemplateDto.setBodyPreview(bodyPreview);
        return emailTemplateDto;
    }

    @Override
    public String previewEmailDefault(PreviewEmailDefaultRequest request) {
        return restTemplateClient.previewEmail(request.getBody(), request.getPlaceholders(), null, true);
    }
}
