package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.service.EmailService;
import nmquan.commonlib.dto.SendEmailDto;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @KafkaListener(topics = "${kafka.topic.send-email}", groupId = "notify-service")
    public void listenSendEmailTopic(String message) {
        SendEmailDto sendEmailDto = ObjectMapperUtils.convertToObject(message, SendEmailDto.class);
        emailService.sendEmail(sendEmailDto);
    }

    @PostMapping("/resend/{emailLogId}")
    public ResponseEntity<Response<Void>> resendEmail(@PathVariable Long emailLogId) {
        emailService.resendEmailClient(emailLogId);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(Messages.IN_PROCESSING_RESEND_EMAIL));
    }
}
