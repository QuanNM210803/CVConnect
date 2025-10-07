package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.EmailLogDto;
import com.cvconnect.enums.SendEmailStatus;
import com.cvconnect.service.EmailLogService;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nmquan.commonlib.dto.SendEmailDto;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailAsyncServiceImpl {
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailLogService emailLogService;

    private static final String EMAIL_LOG_ID_HEADER = "email-log-id";

    @SneakyThrows
    @Async(Constants.BeanName.EMAIL_EXECUTOR)
    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void send(SendEmailDto sendEmailDto, Session session, Long emailLogId) {
        try {
            this.buildMessageAndSend(sendEmailDto, session, emailLogId);
            EmailLogDto existingLog = emailLogService.findById(emailLogId);
            existingLog.setStatus(SendEmailStatus.SUCCESS);
            existingLog.setSentAt(Instant.now());
            emailLogService.save(existingLog);
            log.info("✅ Sent email to: {}", String.join(", ", sendEmailDto.getRecipients()));
        } catch (Exception e) {
            EmailLogDto existingLog = emailLogService.findById(emailLogId);
            existingLog.setStatus(SendEmailStatus.TEMPORARY_FAILURE);
            existingLog.setErrorMessage(e.getMessage());
            emailLogService.save(existingLog);
            log.error("❌ Failed to SEND email: {}", e.getMessage());
            throw new Exception("Failed to SEND email", e);
        }
    }

    // handle when all retry attempts are exhausted
    @Recover
    public void recover(Exception e, SendEmailDto sendEmailDto, Session session, Long emailLogId) {
        EmailLogDto existingLog = emailLogService.findById(emailLogId);
        existingLog.setStatus(SendEmailStatus.FAILURE_WAIT_RESEND);
        existingLog.setErrorMessage(e.getMessage());
        emailLogService.save(existingLog);
    }

    @Async(Constants.BeanName.EMAIL_EXECUTOR)
    public void resend(SendEmailDto sendEmailDto, Session session, Long emailLogId) {
        try {
            this.buildMessageAndSend(sendEmailDto, session, emailLogId);
            EmailLogDto existingLog = emailLogService.findById(emailLogId);
            existingLog.setStatus(SendEmailStatus.SUCCESS);
            existingLog.setSentAt(Instant.now());
            emailLogService.save(existingLog);
            log.info("✅ RESEND email to: {}", String.join(", ", sendEmailDto.getRecipients()));
        } catch (Exception e) {
            EmailLogDto existingLog = emailLogService.findById(emailLogId);
            existingLog.setStatus(SendEmailStatus.FAILURE);
            existingLog.setErrorMessage(e.getMessage());
            emailLogService.save(existingLog);
            log.error("❌ Failed to RESEND email: {}", e.getMessage());
        }
    }

    @SneakyThrows
    private void buildMessageAndSend(SendEmailDto sendEmailDto, Session session, Long emailLogId) {
        InternetAddress[] addresses = this.getInternetAddresses(sendEmailDto.getRecipients());
        InternetAddress[] ccAddresses = this.getInternetAddresses(sendEmailDto.getCcList());
        if(sendEmailDto.getTemplate() != null) {
            Context context = new Context();
            for(Map.Entry<String, String> entry : sendEmailDto.getTemplateVariables().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
            String htmlContent = templateEngine.process(sendEmailDto.getTemplate().getTemplateName(), context);

            MimeMessage message = new MimeMessage(session);
            message.setHeader(EMAIL_LOG_ID_HEADER, emailLogId.toString());
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sendEmailDto.getSender());
            helper.setBcc(addresses);
            helper.setCc(ccAddresses);
            helper.setSubject(sendEmailDto.getSubject());
            helper.setText(htmlContent, true);
            Transport.send(message);
        } else {
//            Message message = new MimeMessage(session);
//            message.setHeader(EMAIL_LOG_ID_HEADER, emailLogId.toString());
//            message.setFrom(new InternetAddress(sendEmailDto.getSender()));
//            message.setRecipients(Message.RecipientType.BCC, addresses);
//            message.setRecipients(Message.RecipientType.CC, ccAddresses);
//            message.setSubject(sendEmailDto.getSubject());
//            message.setText(sendEmailDto.getBody());
//            Transport.send(message);

            MimeMessage message = new MimeMessage(session);
            message.setHeader(EMAIL_LOG_ID_HEADER, emailLogId.toString());
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sendEmailDto.getSender());
            helper.setBcc(addresses);
            helper.setCc(ccAddresses);
            helper.setSubject(sendEmailDto.getSubject());
            helper.setText(sendEmailDto.getBody(), true);
            Transport.send(message);
        }
    }

    private InternetAddress[] getInternetAddresses(List<String> emails) {
        return (emails == null || emails.isEmpty())
                ? new InternetAddress[0]
                : emails.stream()
                .map(email -> {
                    try {
                        return new InternetAddress(email);
                    } catch (AddressException e) {
                        throw new AppException(CommonErrorCode.ERROR);
                    }
                })
                .toArray(InternetAddress[]::new);
    }
}
