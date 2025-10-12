package com.cvconnect.service;

import com.cvconnect.dto.roleUser.RoleUserDto;

import java.util.List;

public interface RoleUserService {
    void createRoleUser(RoleUserDto dto);
    RoleUserDto findByUserIdAndRoleId(Long userId, Long roleId);
    List<RoleUserDto> findRoleUseByUserId(Long userId);
    List<RoleUserDto> findByUserId(Long userId);
    void saveList(List<RoleUserDto> roleUserDtos);
    List<RoleUserDto> getRolesByUserIds(List<Long> userIds);
    void deleteByUserIdAndRoleIds(Long userId, List<Long> roleIds);

}
