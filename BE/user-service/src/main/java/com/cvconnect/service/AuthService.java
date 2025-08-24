package com.cvconnect.service;

import com.cvconnect.dto.LoginRequest;
import com.cvconnect.dto.LoginResponse;
import com.cvconnect.dto.RefreshTokenResponse;
import com.cvconnect.dto.RegisterCandidateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse);
    RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    Long registerCandidate(RegisterCandidateRequest request);
}
