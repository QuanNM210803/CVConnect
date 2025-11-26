package com.cvconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String id;
    private String title;
    private String message;
    private Long senderId;
    private Long receiverId;
    private String type;
    private String redirectUrl;
    private Boolean isRead;
    private Instant readAt;
    private Instant createdAt;

    private List<Long> receiverIds;
}
