package com.cvconnect.collection;

import com.cvconnect.dto.SeenMessage;
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
public class ChatMessage {
    @MongoId
    private String id;

    private String text;
    private Long senderId;
    private Instant sentAt;
    private List<SeenMessage> seenBy;

}
