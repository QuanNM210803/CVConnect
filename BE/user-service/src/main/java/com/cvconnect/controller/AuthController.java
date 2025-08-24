package com.cvconnect.controller;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.LoginRequest;
import com.cvconnect.dto.LoginResponse;
import com.cvconnect.dto.RefreshTokenResponse;
import com.cvconnect.dto.RegisterCandidateRequest;
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

    @PostMapping("/logout")
    @Operation(summary = "Logout API")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse) {
        authService.logout(httpServletRequest, httpServletResponse);
        return ResponseUtils.success(null);
    }

    @PostMapping("/register-candidate")
    @Operation(summary = "Register Candidate API")
    public ResponseEntity<Response<Long>> registerCandidate(@Valid @RequestBody RegisterCandidateRequest request) {
        return ResponseUtils.success(authService.registerCandidate(request),
                localizationUtils.getLocalizedMessage(Messages.REGISTER_SUCCESS));
    }
}
