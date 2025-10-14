package com.cvconnect.service;

import com.cvconnect.dto.auth.*;
import com.cvconnect.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse);
    RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    RegisterCandidateResponse registerCandidate(RegisterCandidateRequest request);
    RegisterOrgAdminResponse registerOrgAdmin(RegisterOrgAdminRequest request, MultipartFile logo, MultipartFile coverPhoto);
    VerifyResponse verify(VerifyRequest verifyRequest);
    RequestResendVerifyEmailResponse requestResendVerifyEmail(String identifier);
    VerifyEmailResponse verifyEmail(String token);
    RequestResetPasswordResponse requestResetPassword(String identifier);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
    void sendRequestVerifyEmail(UserDto userDto);
}
