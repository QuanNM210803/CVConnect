package com.cvconnect.collection;

import com.cvconnect.dto.SeenMessage;
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
public class ChatMessage {
    private String id;

    private String text;
    private Long senderId;
    private Instant sentAt;
    private List<SeenMessage> seenBy;

}
