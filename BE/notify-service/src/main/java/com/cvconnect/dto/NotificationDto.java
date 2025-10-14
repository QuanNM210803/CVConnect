package com.cvconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String id;
    private String title;
    private String message;
    private String senderId;
    private String receiverId;
    private String type;
    private String redirectUrl;
    private Boolean isRead;
    private Instant readAt;
    private Instant createdAt;

}
