package com.cvconnect.controller;

import com.cvconnect.dto.SendEmailDto;
import com.cvconnect.service.EmailService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.send-email}", groupId = "notify-service")
    public void listenSendEmailTopic(String message) {
        SendEmailDto sendEmailDto = ObjectMapperUtils.convertToObject(message, SendEmailDto.class);
        emailService.sendEmail(sendEmailDto);
    }
}
