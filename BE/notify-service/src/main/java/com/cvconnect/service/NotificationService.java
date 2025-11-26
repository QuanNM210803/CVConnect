package com.cvconnect.service;

import com.cvconnect.collection.Notification;
import com.cvconnect.dto.NotificationDto;
import com.cvconnect.dto.NotificationFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

public interface NotificationService {
    void pushNotification(NotificationDto notificationDto);
    List<Notification> save(NotificationDto notificationDto);
    FilterResponse<NotificationDto> getMyNotifications(NotificationFilterRequest request);
    Long getQuantityUnread();
    void markAllAsRead();
    void markAsRead(String notificationId);
}
