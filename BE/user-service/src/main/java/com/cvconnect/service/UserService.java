package com.cvconnect.service;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UpdatePasswordRequest;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.dto.user.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto findById(Long id);
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    UserDto create(UserDto user);
    UserDto getMyInfo(Long roleId);
    void resetPassword(Long userId, String newPassword);
    void updateEmailVerified(Long userId, Boolean emailVerified);
    Boolean checkOrgUserRole(Long userId, String roleCode, Long orgId);
    List<UserDto> getUsersByRoleCodeOrg(String roleCode);
    List<UserDto> getUsersByRoleCodeOrg(String roleCode, Long orgId);
    UserDto getUserById(Long userId);
    void setDefaultRole(Long roleId);
    void updatePassword(UpdatePasswordRequest request);
    void updateAvatar(MultipartFile file);
    void updateInfo(UserUpdateRequest request);
    List<RoleDto> getMyRoles();
}
