package com.cvconnect.service;

import com.cvconnect.dto.managementMember.ManagementMemberDto;

public interface ManagementMemberService {
    ManagementMemberDto getManagementMember(Long userId);
}
