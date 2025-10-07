package com.cvconnect.service;

import com.cvconnect.enums.EmailTemplateEnum;

import java.util.List;
import java.util.Map;

public interface SendEmailService {
    void sendEmailWithTemplate(List<String> recipients, List<String> ccList, EmailTemplateEnum emailTemplate, Map<String, String> templateVariables);
    void sendEmailWithBody(String sender, List<String> recipients, List<String> ccList, String subject, String body, Long orgId);
}
