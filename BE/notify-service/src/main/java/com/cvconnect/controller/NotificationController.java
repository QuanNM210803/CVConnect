package com.cvconnect.controller;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.NotificationDto;
import com.cvconnect.dto.NotificationFilterRequest;
import com.cvconnect.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = Constants.KafkaTopic.NOTIFICATION, groupId = "notify-service")
    public void listenNotification(String message) {
        NotificationDto notification = ObjectMapperUtils.convertToObject(message, NotificationDto.class);
        notificationService.pushNotification(notification);
    }

    @GetMapping("/my-notifications")
    @Operation(summary = "Get my notifications")
    public ResponseEntity<Response<FilterResponse<NotificationDto>>> getMyNotifications(@Valid @ModelAttribute NotificationFilterRequest request) {
        return ResponseUtils.success(notificationService.getMyNotifications(request));
    }

    @GetMapping("/quantity-unread")
    @Operation(summary = "Get quantity of my unread notifications")
    public ResponseEntity<Response<Long>> getQuantityUnreadNotifications() {
        return ResponseUtils.success(notificationService.getQuantityUnread());
    }

    @PutMapping("/mark-all-as-read")
    @Operation(summary = "Mark all my notifications as read")
    public ResponseEntity<Response<Void>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseUtils.success(null);
    }

    @PutMapping("/mark-as-read/{notificationId}")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Response<Void>> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseUtils.success(null);
    }

}
