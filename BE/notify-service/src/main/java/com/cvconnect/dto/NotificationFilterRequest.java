package com.cvconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFilterRequest extends FilterRequest {
    private Long receiverId;
    private Boolean isRead;
    private String type;
    private Instant createdAtStart;
    private Instant createdAtEnd;
}
