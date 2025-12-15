package com.cvconnect.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @MongoId
    private String id;

    private String title;
    private String message;
    private Long senderId;
    private Long receiverId;
    private String receiverType;
    private String type;
    private String redirectUrl;
    private Boolean isRead = false;
    private Instant readAt;
    private Instant createdAt;

}

