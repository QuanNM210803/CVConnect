package com.cvconnect.controller;

import com.cvconnect.dto.user.UserDto;
import com.cvconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
