package com.cvconnect.service;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UpdatePasswordRequest;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.dto.user.UserFilterRequest;
import com.cvconnect.dto.user.UserUpdateRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    FilterResponse<UserDto> findNotOrgMember(UserFilterRequest request);
    Map<Long, UserDto> getByIds(List<Long> userIds);
    FilterResponse<UserDto> filter(UserFilterRequest request);
    UserDto userDetailForSystemAdmin(Long userId);
    void assignAdminSystemRole(Long userId);
    void retrieveAdminSystemRole(Long userId);
    InputStreamResource exportUser(UserFilterRequest filter);
    UserDto getMyProfiles();
    List<UserDto> findAllSystemAdmin();
}
