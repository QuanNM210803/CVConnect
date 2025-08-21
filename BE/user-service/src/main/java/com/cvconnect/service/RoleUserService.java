package com.cvconnect.service;

import com.cvconnect.dto.RoleUserDto;

import java.util.List;

public interface RoleUserService {
    void createRoleUser(RoleUserDto dto);
    RoleUserDto findByUserIdAndRoleId(Long userId, Long roleId);
    List<RoleUserDto> findByUserId(Long userId);

}
