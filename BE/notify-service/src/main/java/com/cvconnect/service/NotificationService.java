package com.cvconnect.service;

import com.cvconnect.dto.NotificationDto;

public interface NotificationService {
    void pushNotification(NotificationDto notificationDto);
    void save(NotificationDto notificationDto);
}
