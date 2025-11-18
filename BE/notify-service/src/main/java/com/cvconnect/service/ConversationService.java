package com.cvconnect.service;

import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.ConversationRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface ConversationService {
    ConversationDto checkExistsConversation(Long jobAdId, Long candidateId);
    IDResponse<String> create(ConversationRequest request);
    Boolean checkExistsConversationWithUnreadMessages();
}
