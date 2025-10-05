package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.InviteUserRequest;
import com.cvconnect.dto.inviteJoinOrg.ReplyInviteUserRequest;
import com.cvconnect.service.OrgMemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/org-member")
public class OrgMemberController {
    @Autowired
    private OrgMemberService orgMemberService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/invite-join-org")
    @Operation(summary = "Invite user to join organization")
    @PreAuthorize("hasAnyAuthority('ORG_MEMBER:ADD')")
    public ResponseEntity<Response<Void>> inviteUserToJoinOrg(@Valid @RequestBody InviteUserRequest request) {
        orgMemberService.inviteUserToJoinOrg(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(Messages.SEND_INVITE_SUCCESS));
    }

    @PostMapping("/reply-invite-join-org")
    @Operation(summary = "Reply invite join organization")
    public ResponseEntity<Response<Void>> replyInviteJoinOrg(@Valid @RequestBody ReplyInviteUserRequest request) {
        orgMemberService.replyInviteJoinOrg(request);
        return ResponseUtils.success(null);
    }
}
