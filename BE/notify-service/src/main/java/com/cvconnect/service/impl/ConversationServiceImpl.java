package com.cvconnect.service.impl;

import com.cvconnect.collection.Conversation;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.ConversationRequest;
import com.cvconnect.enums.NotifyErrorCode;
import com.cvconnect.repository.ConversationRepository;
import com.cvconnect.service.ConversationService;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    public ConversationDto checkExistsConversation(Long jobAdId, Long candidateId) {
        Long userId = WebUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByJobAdIdAndCandidateId(jobAdId, candidateId);
        if(!conversation.getParticipantIds().contains(userId)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        return ObjectMapperUtils.convertToObject(conversation, ConversationDto.class);
    }

    @Override
    @Transactional
    public IDResponse<String> create(ConversationRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByJobAdIdAndCandidateId(request.getJobAdId(), request.getCandidateId());
        if(!ObjectUtils.isEmpty(conversation)) {
            throw new AppException(NotifyErrorCode.CONVERSATION_EXISTED);
        }

        Long hrContactId = restTemplateClient.validateAndGetHrContactId(request.getJobAdId(), request.getCandidateId());
        if(hrContactId == null && !hrContactId.equals(userId) || !request.getCandidateId().equals(userId)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }

        Conversation newConversation = Conversation.builder()
                .jobAdId(request.getJobAdId())
                .candidateId(request.getCandidateId())
                .participantIds(List.of(request.getCandidateId(), hrContactId))
                .createdBy(userId)
                .createdAt(Instant.now())
                .build();
        conversationRepository.save(newConversation);
        return IDResponse.<String>builder()
                .id(newConversation.getId())
                .build();
    }

    @Override
    public Boolean checkExistsConversationWithUnreadMessages() {
        Long userId = WebUtils.getCurrentUserId();
        List<Conversation> conversationOpt = conversationRepository.findAnyUnreadMessage(userId);

        return !ObjectUtils.isEmpty(conversationOpt);
    }
}
