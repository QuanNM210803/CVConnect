package com.cvconnect.service;

import com.cvconnect.dto.common.AssignRoleRequest;
import com.cvconnect.dto.common.InviteUserRequest;
import com.cvconnect.dto.common.ReplyInviteUserRequest;
import com.cvconnect.dto.orgMember.FailedRollbackUpdateAccountStatus;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.orgMember.OrgMemberFilter;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

public interface OrgMemberService {
    OrgMemberDto getOrgMember(Long userId);
    OrgMemberDto getOrgMemberForSystemAdmin(Long userId);
    OrgMemberDto createOrgMember(OrgMemberDto orgMemberDto);
    boolean existsByUserId(Long userId);
    void inviteUserToJoinOrg(InviteUserRequest request);
    void replyInviteJoinOrg(ReplyInviteUserRequest request);
    FilterResponse<OrgMemberDto> filter(OrgMemberFilter request);
    Long validOrgMember();
    void assignRoleOrgMember(AssignRoleRequest request);
    void changeStatusActive(ChangeStatusActiveRequest request);
    OrgMemberDto orgMemberInfo(Long userId);
    Boolean checkOrgMember(List<Long> userIds);
    void updateAccountStatusByOrgIds(ChangeStatusActiveRequest request);
    void rollbackUpdateAccountStatusByOrgIds(FailedRollbackUpdateAccountStatus payload);
    FilterResponse<OrgMemberDto> filterBySystemAdmin(OrgMemberFilter orgMemberFilter);

}
