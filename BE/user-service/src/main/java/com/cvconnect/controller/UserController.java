package com.cvconnect.controller;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UpdatePasswordRequest;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.dto.user.UserFilterRequest;
import com.cvconnect.dto.user.UserUpdateRequest;
import com.cvconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/my-info/{roleId}")
    @Operation(summary = "Get my info by role ID")
    public ResponseEntity<Response<UserDto>> getMyInfo(@PathVariable Long roleId) {
        return ResponseUtils.success(userService.getMyInfo(roleId));
    }

    @InternalRequest
    @GetMapping("/internal/check-org-user-role/{userId}/{roleCode}/{orgId}")
    @Operation(summary = "Check if user has specific role in an organization")
    public ResponseEntity<Response<Boolean>> checkOrgUserRole(@PathVariable Long userId, @PathVariable String roleCode,
                                                              @PathVariable Long orgId) {
        return ResponseUtils.success(userService.checkOrgUserRole(userId, roleCode, orgId));
    }

    // for org-member
    @GetMapping("/get-by-role-code-org/{roleCode}")
    @Operation(summary = "Get users by role code for organization members")
    @PreAuthorize("hasAnyAuthority('ORG_MEMBER:VIEW')")
    public ResponseEntity<Response<List<UserDto>>> getUsersByRoleCodeOrg(@PathVariable String roleCode) {
        return ResponseUtils.success(userService.getUsersByRoleCodeOrg(roleCode));
    }

    @InternalRequest
    @GetMapping("/internal/get-by-id/{userId}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<Response<UserDto>> getUserById(@PathVariable Long userId) {
        return ResponseUtils.success(userService.getUserById(userId));
    }

    @InternalRequest
    @PostMapping("/internal/get-by-ids")
    @Operation(summary = "Get user by IDs")
    public ResponseEntity<Response<Map<Long, UserDto>>> getUserByIds(@RequestBody List<Long> userIds) {
        return ResponseUtils.success(userService.getByIds(userIds));
    }

    @PutMapping("/role-default/{roleId}")
    @Operation(summary = "Set default role for users")
    public ResponseEntity<Response<Void>> setDefaultRole(@PathVariable Long roleId) {
        userService.setDefaultRole(roleId);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update user password")
    public ResponseEntity<Response<Void>> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PutMapping("/update-avatar")
    @Operation(summary = "Update avatar user")
    public ResponseEntity<Response<Void>> updateAvatar(@RequestPart("file") MultipartFile file) {
        userService.updateAvatar(file);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PutMapping("/update-info")
    @Operation(summary = "Update user info")
    public ResponseEntity<Response<Void>> updateInfo(@Valid @RequestBody UserUpdateRequest request) {
        userService.updateInfo(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @GetMapping("/my-roles")
    @Operation(summary = "Get my roles")
    public ResponseEntity<Response<List<RoleDto>>> getMyRoles() {
        return ResponseUtils.success(userService.getMyRoles());
    }

    @GetMapping("/find-not-org-member")
    @Operation(summary = "Find users who are not organization members")
    @PreAuthorize("hasAnyAuthority('ORG_MEMBER:VIEW', 'USER_GROUP:VIEW')")
    public ResponseEntity<Response<FilterResponse<UserDto>>> findNotOrgMember(@Valid @ModelAttribute UserFilterRequest request) {
        return ResponseUtils.success(userService.findNotOrgMember(request));
    }
}
