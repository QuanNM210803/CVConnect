package com.cvconnect.service;

import com.cvconnect.dto.role.MemberTypeDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.role.RoleFilterRequest;
import com.cvconnect.dto.role.RoleRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface RoleService {
    RoleDto getRoleByCode(String code);
    RoleDto getRoleById(Long id);
    List<MemberTypeDto> getAllMemberTypes();
    IDResponse<Long> createRoles(RoleRequest request);
    IDResponse<Long> updateRoles(RoleRequest request);
    FilterResponse<RoleDto> filter(RoleFilterRequest request);
    void deleteByIds(List<Long> ids);
    List<RoleDto> getRoleByUserId(Long userId);
}
