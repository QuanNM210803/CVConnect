package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    @NotNull(message = Messages.CONVERSATION_APPLY_REQUIRED)
    private Long jobAdId;
    @NotNull(message = Messages.CONVERSATION_APPLY_REQUIRED)
    private Long candidateId;
}
