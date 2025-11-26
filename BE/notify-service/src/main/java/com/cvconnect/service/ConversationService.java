package com.cvconnect.service;

import com.cvconnect.dto.ChatMessageFilter;
import com.cvconnect.dto.ChatMessageRequest;
import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.ConversationRequest;
import com.cvconnect.dto.internal.request.MyConversationWithFilter;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface ConversationService {
    ConversationDto checkExistsConversation(Long jobAdId, Long candidateId);
    IDResponse<String> create(ConversationRequest request);
    Boolean checkExistsConversationWithUnreadMessages(Boolean isCandidate);
    List<ConversationDto> getConversationsWithUnreadMessages();
    List<ConversationDto> getMyConversations();
    FilterResponse<ConversationDto> getMyConversationsWithFilter(MyConversationWithFilter filter);
    ConversationDto getChatMessages(ChatMessageFilter filter);
    void readAllMessages(ConversationRequest request);
    IDResponse<String> newMessage(ChatMessageRequest request);
}
