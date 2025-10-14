package com.cvconnect.service.impl;

import com.cvconnect.collection.Notification;
import com.cvconnect.dto.NotificationDto;
import com.cvconnect.repository.NotificationRepository;
import com.cvconnect.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void pushNotification(NotificationDto notificationDto) {
        this.save(notificationDto);

        // todo: push notification to user via websocket
    }

    @Override
    public void save(NotificationDto notificationDto) {
        Notification notification = new Notification();
        notification.setTitle(notificationDto.getTitle());
        notification.setMessage(notificationDto.getMessage());
        notification.setSenderId(notificationDto.getSenderId());
        notification.setReceiverId(notificationDto.getReceiverId());
        notification.setType(notificationDto.getType());
        notification.setRedirectUrl(notificationDto.getRedirectUrl());
        notification.setIsRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(notificationDto.getCreatedAt() == null ? Instant.now() : notificationDto.getCreatedAt());
        notificationRepository.save(notification);
    }
}
