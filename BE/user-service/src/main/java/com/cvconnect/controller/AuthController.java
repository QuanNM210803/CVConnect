package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.LoginRequest;
import com.cvconnect.dto.LoginResponse;
import com.cvconnect.dto.RefreshTokenResponse;
import com.cvconnect.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/login")
    @Operation(summary = "Login API", description = "Authenticate user and return access token")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                         HttpServletResponse httpServletResponse) {
        return ResponseUtils.success(authService.login(loginRequest, httpServletResponse),
                localizationUtils.getLocalizedMessage(Messages.LOGIN_SUCCESS));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token API")
    public ResponseEntity<Response<RefreshTokenResponse>> refreshToken(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse) {
        return ResponseUtils.success(authService.refreshToken(httpServletRequest, httpServletResponse));
    }
}
