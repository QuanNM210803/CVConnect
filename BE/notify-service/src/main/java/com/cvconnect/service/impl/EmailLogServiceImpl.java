package com.cvconnect.service.impl;

import com.cvconnect.dto.EmailLogDto;
import com.cvconnect.entity.EmailLog;
import com.cvconnect.enums.SendEmailStatus;
import com.cvconnect.repository.EmailLogRepository;
import com.cvconnect.service.EmailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailLogServiceImpl implements EmailLogService {
    @Autowired
    private EmailLogRepository emailLogRepository;

    @Override
    public Long save(EmailLogDto emailLogDto) {
        EmailLog emailLog = new EmailLog();
        emailLog.setId(emailLogDto.getId());
        emailLog.setMessageId(emailLogDto.getMessageId());
        emailLog.setSender(emailLogDto.getSender());
        emailLog.setEmailGroup(emailLogDto.getEmailGroup());
        emailLog.setRecipients(emailLogDto.getRecipients());
        emailLog.setCcList(emailLogDto.getCcList());
        emailLog.setSubject(emailLogDto.getSubject());
        emailLog.setBody(emailLogDto.getBody());
        emailLog.setTemplate(emailLogDto.getTemplate());
        emailLog.setTemplateVariables(emailLogDto.getTemplateVariables());
        emailLog.setErrorMessage(emailLogDto.getErrorMessage());
        emailLog.setStatus(emailLogDto.getStatus());
        emailLog.setSentAt(emailLogDto.getSentAt());
        emailLog.setCreatedBy(emailLogDto.getCreatedBy());
        emailLog.setUpdatedBy(emailLogDto.getUpdatedBy());
        emailLogRepository.save(emailLog);
        return emailLog.getId();
    }

    @Override
    public EmailLogDto findById(Long id) {
        EmailLog emailLog = emailLogRepository.findById(id).orElse(null);
        if (emailLog == null) {
            return null;
        }
        return this.buildEmailLogDto(emailLog);
    }

    @Override
    public List<EmailLogDto> getWaitResendEmail(Long limit) {
        if(limit == null){
            limit = 50L;
        }
        List<EmailLog> emailLogs = emailLogRepository.findByStatus(SendEmailStatus.FAILURE_WAIT_RESEND, limit);
        return emailLogs.stream()
                .map(this::buildEmailLogDto).toList();
    }

    private EmailLogDto buildEmailLogDto(EmailLog emailLog) {
        EmailLogDto emailLogDto = new EmailLogDto();
        emailLogDto.setId(emailLog.getId());
        emailLogDto.setMessageId(emailLog.getMessageId());
        emailLogDto.setReplyMessageId(emailLogDto.getReplyMessageId());
        emailLogDto.setEmailGroup(emailLog.getEmailGroup());
        emailLogDto.setSender(emailLog.getSender());
        emailLogDto.setRecipients(emailLog.getRecipients());
        emailLogDto.setCcList(emailLog.getCcList());
        emailLogDto.setSubject(emailLog.getSubject());
        emailLogDto.setBody(emailLog.getBody());
        emailLogDto.setTemplate(emailLog.getTemplate());
        emailLogDto.setTemplateVariables(emailLog.getTemplateVariables());
        emailLogDto.setErrorMessage(emailLog.getErrorMessage());
        emailLogDto.setStatus(emailLog.getStatus());
        emailLogDto.setSentAt(emailLog.getSentAt());
        emailLogDto.setIsActive(emailLog.getIsActive());
        emailLogDto.setIsDeleted(emailLog.getIsDeleted());
        emailLogDto.setCreatedBy(emailLog.getCreatedBy());
        emailLogDto.setUpdatedBy(emailLog.getUpdatedBy());
        emailLogDto.setCreatedAt(emailLog.getCreatedAt());
        emailLogDto.setUpdatedAt(emailLog.getUpdatedAt());
        return emailLogDto;
    }
}
