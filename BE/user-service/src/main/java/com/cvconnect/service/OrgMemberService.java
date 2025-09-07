package com.cvconnect.service;

import com.cvconnect.dto.orgMember.OrgMemberDto;

public interface OrgMemberService {
    OrgMemberDto getOrgMember(Long userId);
    OrgMemberDto createOrgMember(OrgMemberDto orgMemberDto);
    boolean existsByUserId(Long userId);
}
