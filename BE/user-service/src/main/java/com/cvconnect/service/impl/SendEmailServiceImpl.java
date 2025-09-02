package com.cvconnect.service.impl;

import com.cvconnect.dto.SendEmailDto;
import com.cvconnect.enums.EmailTemplateEnum;
import com.cvconnect.service.SendEmailService;
import com.cvconnect.utils.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private KafkaUtils kafkaUtils;

    @Value("${server.email-admin}")
    private String EMAIL_ADMIN;
    @Value("${kafka.topic.send-email}")
    private String TOPIC_SEND_EMAIL;

    @Override
    public void sendEmailWithTemplate(List<String> recipients, List<String> ccList, EmailTemplateEnum emailTemplate, Map<String, String> templateVariables) {
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .sender(EMAIL_ADMIN)
                .recipients(recipients)
                .ccList(ccList)
                .subject(emailTemplate.getSubject())
                .template(emailTemplate)
                .templateVariables(templateVariables)
                .build();
        kafkaUtils.sendWithJson(TOPIC_SEND_EMAIL, sendEmailDto);
    }

    @Override
    public void sendEmailWithBody(List<String> recipients, String subject, String body) {

    }
}
