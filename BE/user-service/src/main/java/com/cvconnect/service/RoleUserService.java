package com.cvconnect.service;

import com.cvconnect.dto.RoleUserDto;

public interface RoleUserService {
    void createRoleUser(RoleUserDto dto);
    RoleUserDto findByUserIdAndRoleId(Long userId, Long roleId);
}
