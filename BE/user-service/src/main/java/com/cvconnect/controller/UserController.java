package com.cvconnect.controller;

import com.cvconnect.dto.user.UserDto;
import com.cvconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
}
