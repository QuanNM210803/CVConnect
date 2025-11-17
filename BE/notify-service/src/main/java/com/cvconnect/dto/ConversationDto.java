package com.cvconnect.dto;

import com.cvconnect.collection.ChatMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationDto {
    private String id;
    private Long jobAdId;
    private Long candidateId;

    private List<Long> participantIds;
    private Long createdBy;
    private Instant createdAt;
    private List<ChatMessage> messages;
}
