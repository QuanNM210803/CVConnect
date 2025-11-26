package com.cvconnect.dto.internal.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyConversationWithFilter {
    private Boolean hasMessagesUnread;
    Long userId;

    private Instant pageIndex = Instant.now();
    private Integer pageSize = 20 ;
}
