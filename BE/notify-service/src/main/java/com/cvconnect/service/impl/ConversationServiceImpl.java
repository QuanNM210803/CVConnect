package com.cvconnect.service.impl;

import com.cvconnect.dto.ConversationDto;
import com.cvconnect.repository.ConversationRepository;
import com.cvconnect.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public ConversationDto checkExistsConversation(Long jobAdId, Long candidateId) {
        return null;
    }
}
