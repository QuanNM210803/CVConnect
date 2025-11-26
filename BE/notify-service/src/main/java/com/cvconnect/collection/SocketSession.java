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
@Document(collection = "socket_sessions")
public class SocketSession {
    @MongoId
    private String id;
    private String sessionId;
    private Long userId;
    private Instant createdAt;
}
