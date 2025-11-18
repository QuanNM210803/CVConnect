package com.cvconnect.controller;

import com.cvconnect.dto.ConversationDto;
import com.cvconnect.dto.ConversationRequest;
import com.cvconnect.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response<Boolean>> checkExistsConversationWithUnreadMessages() {
        return ResponseUtils.success(conversationService.checkExistsConversationWithUnreadMessages());
    }

}
