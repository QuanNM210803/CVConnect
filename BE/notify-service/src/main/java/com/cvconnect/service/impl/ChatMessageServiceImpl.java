package com.cvconnect.service.impl;

import com.cvconnect.repository.ChatMessageRepository;
import com.cvconnect.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
}
