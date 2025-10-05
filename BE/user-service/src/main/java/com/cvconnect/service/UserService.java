package com.cvconnect.service;

import com.cvconnect.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto findById(Long id);
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    UserDto create(UserDto user);
    UserDto getMyInfo(Long roleId);
    void updatePassword(Long userId, String newPassword);
    void updateEmailVerified(Long userId, Boolean emailVerified);
    Boolean checkOrgUserRole(Long userId, String roleCode, Long orgId);
    List<UserDto> getUsersByRoleCodeOrg(String roleCode);
}
