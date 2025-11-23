package com.cvconnect.controller;

import com.cvconnect.dto.ChatMessageFilter;
import com.cvconnect.dto.ChatMessageRequest;
import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.ConversationRequest;
import com.cvconnect.dto.internal.request.MyConversationWithFilter;
import com.cvconnect.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/check-exists/{jobAdId}/{candidateId}")
    @Operation(summary = "Check exists conversation between job ad and candidate")
    public ResponseEntity<Response<ConversationDto>> getConversation(@PathVariable Long jobAdId, @PathVariable Long candidateId) {
        return ResponseUtils.success(conversationService.checkExistsConversation(jobAdId, candidateId));
    }

    @PostMapping("/create")
    @Operation(summary = "Create conversation between job ad and candidate")
    public ResponseEntity<Response<IDResponse<String>>> createConversation(@Valid @RequestBody ConversationRequest request) {
        return ResponseUtils.success(conversationService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @GetMapping("/check-exists-message-unread")
    @Operation(summary = "Check exists conversation with unread messages")
    public ResponseEntity<Response<Boolean>> checkExistsConversationWithUnreadMessages(@RequestParam Boolean isCandidate) {
        return ResponseUtils.success(conversationService.checkExistsConversationWithUnreadMessages(isCandidate));
    }

    // view candidate: job-ad-applied
    @GetMapping("/internal/conversation-unread")
    @InternalRequest
    @Operation(summary = "Get conversations with unread messages")
    public ResponseEntity<Response<List<ConversationDto>>> getConversationsWithUnreadMessages() {
        return ResponseUtils.success(conversationService.getConversationsWithUnreadMessages());
    }

    // view candidate: list-conversation no page
    @GetMapping("/internal/my-conversations")
    @InternalRequest
    @Operation(summary = "Get my conversations")
    public ResponseEntity<Response<List<ConversationDto>>> getMyConversations() {
        return ResponseUtils.success(conversationService.getMyConversations());
    }

    // view HR: list-conversation with filter and page
    @PostMapping("/internal/my-conversations-filtered")
    @InternalRequest
    @Operation(summary = "Get my conversations filtered")
    public ResponseEntity<Response<FilterResponse<ConversationDto>>> getMyConversationsWithFilter(@Valid @RequestBody MyConversationWithFilter request) {
        return ResponseUtils.success(conversationService.getMyConversationsWithFilter(request));
    }

    @GetMapping("/chat-messages")
    @Operation(summary = "Get chat messages between candidate and hr contact for a job ad")
    public ResponseEntity<Response<ConversationDto>> getChatMessages(@Valid @ModelAttribute ChatMessageFilter filter) {
        return ResponseUtils.success(conversationService.getChatMessages(filter));
    }

    @PostMapping("/read-all-messages")
    @Operation(summary = "Mark all messages in a conversation as read")
    public ResponseEntity<Response<Void>> readAllMessages(@Valid @RequestBody ConversationRequest request) {
        conversationService.readAllMessages(request);
        return ResponseUtils.success(null);
    }

    @PostMapping("/new-message")
    @Operation(summary = "New message in conversation")
    public ResponseEntity<Response<IDResponse<String>>> newMessageInConversation(@Valid @RequestBody ChatMessageRequest request) {
        return ResponseUtils.success(conversationService.newMessage(request));
    }

}
