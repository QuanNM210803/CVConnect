package com.cvconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageFilter {
    private Long jobAdId;
    private Long candidateId;
    private Instant pageIndex = Instant.now();
    private Integer pageSize = 20 ;
}
