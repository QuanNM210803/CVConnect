package com.cvconnect.service.impl;

import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.dto.EmailLogDto;
import com.cvconnect.enums.NotifyErrorCode;
import com.cvconnect.enums.SendEmailStatus;
import com.cvconnect.service.EmailConfigService;
import com.cvconnect.service.EmailLogService;
import com.cvconnect.service.EmailService;
import jakarta.mail.*;
import nmquan.commonlib.dto.SendEmailDto;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private EmailLogService emailLogService;
    @Autowired
    private EmailAsyncServiceImpl emailAsyncServiceImpl;

    private static final int BATCH_SIZE = 30;

    @Override
    public void sendEmail(SendEmailDto sendEmailDto) {
        Session session = this.getSession(sendEmailDto.getOrgId());
        List<String> recipients = sendEmailDto.getRecipients();
        String emailGroup = UUID.randomUUID().toString().substring(0,18);
        for (int i = 0; i < recipients.size(); i += BATCH_SIZE) {
            List<String> batch = recipients.subList(i, Math.min(i + BATCH_SIZE, recipients.size()));
            SendEmailDto batchDto = SendEmailDto.builder()
                    .sender(sendEmailDto.getSender())
                    .recipients(batch)
                    .ccList(sendEmailDto.getCcList())
                    .subject(sendEmailDto.getSubject())
                    .body(sendEmailDto.getBody())
                    .candidateInfoId(sendEmailDto.getCandidateInfoId())
                    .jobAdId(sendEmailDto.getJobAdId())
                    .orgId(sendEmailDto.getOrgId())
                    .emailTemplateId(sendEmailDto.getEmailTemplateId())
                    .template(sendEmailDto.getTemplate())
                    .templateVariables(sendEmailDto.getTemplateVariables())
                    .build();
            EmailLogDto emailLogDto = this.buildEmailLogDto(batchDto, emailGroup);
            Long emailLogId = emailLogService.save(emailLogDto);
            // Send email async
            emailAsyncServiceImpl.send(batchDto, session, emailLogId);
        }
    }

    @Override
    public void resendEmail(SendEmailDto sendEmailDto, Long emailLogId) {
        Session session = this.getSession(sendEmailDto.getOrgId());
        // Send email async
        emailAsyncServiceImpl.resend(sendEmailDto, session, emailLogId);
    }

    private Session getSession(Long orgId) {
        EmailConfigDto emailConfigDto = emailConfigService.getByOrgId(orgId);
        if (emailConfigDto == null) {
            throw new AppException(NotifyErrorCode.EMAIL_CONFIG_NOT_FOUND);
        }

        Properties props = new Properties();
        props.put("mail.transport.protocol", emailConfigDto.getProtocol());
        props.put("mail." + emailConfigDto.getProtocol() + ".host", emailConfigDto.getHost());
        props.put("mail." + emailConfigDto.getProtocol() + ".port", String.valueOf(emailConfigDto.getPort()));
        props.put("mail." + emailConfigDto.getProtocol() + ".auth", "true");
        if (emailConfigDto.getIsSsl()) {
            props.put("mail." + emailConfigDto.getProtocol() + ".ssl.enable", "true");
        } else {
            props.put("mail." + emailConfigDto.getProtocol() + ".starttls.enable", "true");
        }
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfigDto.getEmail(), emailConfigDto.getPassword());
            }
        });
    }

    private EmailLogDto buildEmailLogDto(SendEmailDto sendEmailDto, String emailGroup) {
        return EmailLogDto.builder()
                .emailGroup(emailGroup)
                .sender(sendEmailDto.getSender())
                .recipients(String.join(",", sendEmailDto.getRecipients()))
                .ccList(sendEmailDto.getCcList() == null ? null : String.join(",", sendEmailDto.getCcList()))
                .subject(sendEmailDto.getSubject())
                .body(sendEmailDto.getBody())
                .candidateInfoId(sendEmailDto.getCandidateInfoId())
                .jobAdId(sendEmailDto.getJobAdId())
                .orgId(sendEmailDto.getOrgId())
                .emailTemplateId(sendEmailDto.getEmailTemplateId())
                .template(sendEmailDto.getTemplate() == null ? null : sendEmailDto.getTemplate())
                .templateVariables(sendEmailDto.getTemplateVariables() == null ? null : ObjectMapperUtils.convertToJson(sendEmailDto.getTemplateVariables()))
                .status(SendEmailStatus.SENDING)
                .build();
    }
}
