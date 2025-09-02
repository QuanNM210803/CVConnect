package com.cvconnect.service;

import com.cvconnect.dto.auth.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse);
    RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    RegisterCandidateResponse registerCandidate(RegisterCandidateRequest request);
    VerifyResponse verify(VerifyRequest verifyRequest);
    RequestResendVerifyEmailResponse requestResendVerifyEmail(String identifier);
    VerifyEmailResponse verifyEmail(String token);
    RequestResetPasswordResponse requestResetPassword(String identifier);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}
