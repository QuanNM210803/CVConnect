package com.cvconnect.service;

import com.cvconnect.dto.roleMenu.PermissionTypeDto;
import com.cvconnect.dto.roleMenu.RoleMenuDto;

import java.util.List;
import java.util.Map;

public interface RoleMenuService {
    Map<String, List<String>> getAuthorities(Long userId);
    List<PermissionTypeDto> getAllPermissionTypes();
    void saveAll(List<RoleMenuDto> roleMenuDtos);
    void deleteByRoleId(Long roleId);
}
