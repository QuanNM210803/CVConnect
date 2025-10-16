package com.cvconnect.service;

import com.cvconnect.dto.role.MemberTypeDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.role.RoleFilterRequest;
import com.cvconnect.dto.role.RoleRequest;
import com.cvconnect.enums.MemberType;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleDto getRoleByCode(String code);
    RoleDto getRoleById(Long id);
    List<MemberTypeDto> getAllMemberTypes();
    IDResponse<Long> createRoles(RoleRequest request);
    IDResponse<Long> updateRoles(RoleRequest request);
    FilterResponse<RoleDto> filter(RoleFilterRequest request);
    void deleteByIds(List<Long> ids);
    List<RoleDto> getRoleByUserId(Long userId);
    List<RoleDto> getRoleUseByUserId(Long userId);
    RoleDto getDetail(Long id);
    Map<Long, List<RoleDto>> getRolesByUserIds(List<Long> userIds);
    List<RoleDto> getRoleByIds(List<Long> ids);
    List<RoleDto> getByMemberType(MemberType memberType);
}
