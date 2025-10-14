package com.cvconnect.controller;

import com.cvconnect.dto.NotificationDto;
import com.cvconnect.service.NotificationService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "${kafka.topic.notification}", groupId = "notify-service")
    public void listenNotification(String message) {
        NotificationDto notification = ObjectMapperUtils.convertToObject(message, NotificationDto.class);
        notificationService.pushNotification(notification);
    }
}
