package com.cvconnect.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class Conversation {
    @MongoId
    private String id;

    // unique
    private Long jobAdId;
    private Long candidateId; // userId

    private List<Long> participantIds;
    private Long createdBy;
    private Instant createdAt;
    private List<ChatMessage> messages;
}
