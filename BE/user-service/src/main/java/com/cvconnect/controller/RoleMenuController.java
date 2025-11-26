package com.cvconnect.controller;

import com.cvconnect.dto.roleMenu.PermissionTypeDto;
import com.cvconnect.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping("/permission-type")
    @Operation(summary = "Get all permission types")
    @PreAuthorize("hasAnyAuthority('USER_GROUP:VIEW')")
    public ResponseEntity<Response<List<PermissionTypeDto>>> getAllPermissionTypes() {
        return ResponseUtils.success(roleMenuService.getAllPermissionTypes());
    }

}
