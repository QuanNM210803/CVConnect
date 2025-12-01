package com.cvconnect.service.impl;

import com.cvconnect.collection.ChatMessage;
import com.cvconnect.collection.Conversation;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.config.socket.SocketHandler;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.*;
import com.cvconnect.dto.internal.request.MyConversationWithFilter;
import com.cvconnect.enums.NotifyErrorCode;
import com.cvconnect.enums.RoomSocketType;
import com.cvconnect.repository.ConversationRepository;
import com.cvconnect.service.ConversationService;
import com.cvconnect.service.MongoQueryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nmquan.commonlib.dto.PageInfo;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private MongoQueryService mongoQueryService;
    @Lazy
    @Autowired
    private SocketHandler socketHandler;

    @Override
    public ConversationDto checkExistsConversation(Long jobAdId, Long candidateId) {
        Long userId = WebUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByJobAdIdAndCandidateId(jobAdId, candidateId);
        if(ObjectUtils.isEmpty(conversation)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
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
        if(hrContactId == null || (!hrContactId.equals(userId) && !request.getCandidateId().equals(userId))) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        if(hrContactId.equals(request.getCandidateId())) {
            throw new AppException(NotifyErrorCode.INVALID_CREATE_CONVERSATION_HR_CONTACT);
        }

        Conversation newConversation = Conversation.builder()
                .jobAdId(request.getJobAdId())
                .candidateId(request.getCandidateId())
                .participantIds(List.of(request.getCandidateId(), hrContactId))
                .createdBy(userId)
                .createdAt(Instant.now())
                .build();
        conversationRepository.save(newConversation);

        // socket
        Long roomId = request.getCandidateId().equals(userId) ? hrContactId : request.getCandidateId();
        socketHandler.sendEventWithRoom(
                Map.of("jobAdId", request.getJobAdId(),
                        "candidateId", request.getCandidateId()),
                Constants.SocketTopic.NEW_CONVERSATION, RoomSocketType.USER.getPrefix() + roomId
        );
        return IDResponse.<String>builder()
                .id(newConversation.getId())
                .build();
    }

    @Override
    public Boolean checkExistsConversationWithUnreadMessages(Boolean isCandidate) {
        Long userId = WebUtils.getCurrentUserId();
        if(isCandidate == null) {
            throw new AppException(CommonErrorCode.INVALID_FORMAT);
        }
        List<Conversation> conversationOpt;
        if(isCandidate) {
            conversationOpt = conversationRepository.findAnyUnreadMessageCandidate(userId);
        } else {
            conversationOpt = conversationRepository.findAnyUnreadMessageHr(userId);
        }

        return !ObjectUtils.isEmpty(conversationOpt);
    }

    @Override
    public List<ConversationDto> getConversationsWithUnreadMessages() {
        Long userId = WebUtils.getCurrentUserId();
        List<Conversation> conversationOpt = conversationRepository.findAnyUnreadMessageCandidate(userId);
        List<ConversationDto> conversationDtos = conversationOpt.stream()
                .map(c -> {
                    ConversationDto conversationDto = new ConversationDto();
                    conversationDto.setId(c.getId());
                    conversationDto.setJobAdId(c.getJobAdId());
                    conversationDto.setCandidateId(c.getCandidateId());
                    return conversationDto;
                }).toList();
        if(ObjectUtils.isEmpty(conversationDtos)) {
            return List.of();
        }
        return conversationDtos;
    }

    @Override
    public List<ConversationDto> getMyConversations() {
        Long userId = WebUtils.getCurrentUserId();
        return conversationRepository.findConversationByCandidate(userId);
    }

    @Override
    public FilterResponse<ConversationDto> getMyConversationsWithFilter(@Valid @RequestBody MyConversationWithFilter filter) {
        Long userId = WebUtils.getCurrentUserId();
        filter.setUserId(userId);
        return mongoQueryService.getMyConversationsWithFilter(filter, filter.getPageIndex(), filter.getPageSize());
    }

    @Override
    public ConversationDto getChatMessages(ChatMessageFilter filter) {
        Long userId = WebUtils.getCurrentUserId();
        ConversationDto response = mongoQueryService.getChatMessages(filter);
        if(!response.getParticipantIds().contains(userId)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        Conversation conversation = conversationRepository.findById(response.getId()).orElse(null);
        if(conversation == null) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        if(!ObjectUtils.isEmpty(conversation.getMessages())) {
            if(!ObjectUtils.isEmpty(response.getMessagesWithFilter())) {
                PageInfo pageInfo = PageInfo.builder()
                        .totalElements((long) conversation.getMessages().size())
                        .build();
                response.getMessagesWithFilter().setPageInfo(pageInfo);
            }
        }
        return response;
    }

    @Override
    public void readAllMessages(ConversationRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByJobAdIdAndCandidateId(request.getJobAdId(), request.getCandidateId());
        if(ObjectUtils.isEmpty(conversation) || !conversation.getParticipantIds().contains(userId)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        if(ObjectUtils.isEmpty(conversation.getMessages())) {
            return;
        }
        conversationRepository.markAllMessagesAsRead(conversation.getId(), userId);

        // socket
        for(Long participantId : conversation.getParticipantIds()) {
            if(!participantId.equals(userId)) {
                socketHandler.sendEventWithRoom(
                        Map.of("conversationId", conversation.getId(),
                                "jobAdId", request.getJobAdId(),
                                "candidateId", request.getCandidateId()),
                        Constants.SocketTopic.READ_ALL_MESSAGES,
                        RoomSocketType.USER.getPrefix() + participantId
                );
            }
        }
    }

    @Override
    public IDResponse<String> newMessage(ChatMessageRequest request, Long userId) {
        if(userId == null) {
            userId = WebUtils.getCurrentUserId();
        }
        Conversation conversation = conversationRepository.findByJobAdIdAndCandidateId(request.getJobAdId(), request.getCandidateId());
        if(ObjectUtils.isEmpty(conversation) || !conversation.getParticipantIds().contains(userId)) {
            throw new AppException(CommonErrorCode.DATA_NOT_FOUND);
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(new ObjectId().toHexString());
        chatMessage.setSenderId(userId);
        chatMessage.setText(request.getText());
        chatMessage.setSentAt(Instant.now());
        chatMessage.setSeenBy(List.of(userId));

        if(ObjectUtils.isEmpty(conversation.getMessages())) {
            conversation.setMessages(new ArrayList<>());
        }

        conversation.getMessages().add(chatMessage);
        conversationRepository.save(conversation);

        // socket
        DataJobAdCandidate dataJobAdCandidate = restTemplateClient.getJobAdCandidateData(request.getJobAdId(), request.getCandidateId());
        Map<String, Object> params = new HashMap<>();
        params.put("newMessage", chatMessage);
        params.put("jobAdId", request.getJobAdId());
        params.put("title", dataJobAdCandidate.getJobAdTitle());
        params.put("candidateId", request.getCandidateId());
        params.put("fullName", dataJobAdCandidate.getFullName());
        for(Long participantId : conversation.getParticipantIds()) {
            if(!participantId.equals(userId)) {
                socketHandler.sendEventWithRoom(
                        params,
                        Constants.SocketTopic.NEW_MESSAGE,
                        RoomSocketType.USER.getPrefix() + participantId
                );
            }
        }

        return IDResponse.<String>builder()
                .id(chatMessage.getId())
                .build();
    }
}
