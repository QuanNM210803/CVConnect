package com.cvconnect.service.impl;

import com.cvconnect.dto.LoginRequest;
import com.cvconnect.dto.LoginResponse;
import com.cvconnect.dto.RefreshTokenResponse;
import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        return null;
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }
}
