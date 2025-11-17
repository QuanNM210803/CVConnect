package com.cvconnect.service;

import com.cvconnect.dto.ConversationDto;

public interface ConversationService {
    ConversationDto checkExistsConversation(Long jobAdId, Long candidateId);
}
