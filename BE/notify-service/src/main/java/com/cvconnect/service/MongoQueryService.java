package com.cvconnect.service;

import com.cvconnect.collection.Notification;
import com.cvconnect.dto.NotificationFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MongoQueryService {
    Page<Notification> findNotificationWithFilter(NotificationFilterRequest request, Pageable pageable);
}
