package com.cvconnect.service;

import com.cvconnect.dto.InviteUserRequest;
import com.cvconnect.dto.inviteJoinOrg.ReplyInviteUserRequest;
import com.cvconnect.dto.orgMember.OrgMemberDto;

public interface OrgMemberService {
    OrgMemberDto getOrgMember(Long userId);
    OrgMemberDto createOrgMember(OrgMemberDto orgMemberDto);
    boolean existsByUserId(Long userId);
    void inviteUserToJoinOrg(InviteUserRequest request);
    void replyInviteJoinOrg(ReplyInviteUserRequest request);
}
