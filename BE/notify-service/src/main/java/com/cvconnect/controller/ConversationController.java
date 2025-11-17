package com.cvconnect.controller;

import com.cvconnect.dto.ConversationDto;
import com.cvconnect.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/check-exists/{jobAdId}/{candidateId}")
    @Operation(summary = "Check exists conversation between job ad and candidate")
    public ResponseEntity<Response<ConversationDto>> getConversation(@PathVariable Long jobAdId, @PathVariable Long candidateId) {
        return ResponseUtils.success(conversationService.checkExistsConversation(jobAdId, candidateId));
    }

}
