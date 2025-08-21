package com.cvconnect.service.impl;

import com.cvconnect.config.security.JwtUtils;
import com.cvconnect.dto.LoginRequest;
import com.cvconnect.dto.LoginResponse;
import com.cvconnect.dto.RefreshTokenResponse;
import com.cvconnect.dto.RoleUserDto;
import com.cvconnect.entity.User;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.service.AuthService;
import com.cvconnect.service.RoleUserService;
import com.cvconnect.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    private static final String NAME_COOKIE_REFRESH_TOKEN = "refreshToken";
    @Value("${jwt.refresh-expiration}")
    private int JWT_REFRESHABLE_DURATION;

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        User user = userService.findByUsername(loginRequest.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AppException(UserErrorCode.LOGIN_FAIL);
        }

        List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(user.getId());
        LoginResponse loginResponse=LoginResponse.builder()
                .token(jwtUtils.generateToken(user))
                .roles(roleUserDtos.stream().map(RoleUserDto::getRole).collect(Collectors.toList()))
                .build();

        String refreshToken = jwtUtils.generateRefreshToken(user);
        this.setRefreshTokenCookie(refreshToken, httpServletResponse);

        return loginResponse;
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }

    private void setRefreshTokenCookie(String refreshToken,HttpServletResponse httpServletResponse){
        ResponseCookie responseCookie = ResponseCookie
                .from(NAME_COOKIE_REFRESH_TOKEN, refreshToken)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(JWT_REFRESHABLE_DURATION)
                .sameSite("Lax")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void deleteRefreshTokenCookie(HttpServletResponse httpServletResponse){
        ResponseCookie responseCookie = ResponseCookie
                .from(NAME_COOKIE_REFRESH_TOKEN, null)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private String getRefreshTokenCookie(HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        if(!Objects.isNull(cookies)){
            for(Cookie cookie:cookies){
                if(NAME_COOKIE_REFRESH_TOKEN.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
