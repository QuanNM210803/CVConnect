package com.cvconnect.service;

import com.cvconnect.collection.Notification;
import com.cvconnect.dto.ChatMessageFilter;
import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.NotificationFilterRequest;
import com.cvconnect.dto.internal.request.MyConversationWithFilter;
import nmquan.commonlib.dto.response.FilterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface MongoQueryService {
    Page<Notification> findNotificationWithFilter(NotificationFilterRequest request, Pageable pageable);
    FilterResponse<ConversationDto> getMyConversationsWithFilter(MyConversationWithFilter request, Instant pageIndex, Integer pageSize);
    ConversationDto getChatMessages(ChatMessageFilter filter);
}
