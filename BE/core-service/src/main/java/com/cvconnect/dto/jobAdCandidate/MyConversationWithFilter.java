package com.cvconnect.dto.jobAdCandidate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyConversationWithFilter {
    private Boolean hasMessagesUnread;
    Long userId;

    private Instant pageIndex = Instant.now();
    private Integer pageSize = 20;
}
