package com.cvconnect.utils;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.service.RoleMenuService;
import com.cvconnect.service.RoleService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.expiration}")
    private int EXPIRATION;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private final RoleMenuService roleMenuService;
    private final RoleService roleService;

    // functions to generate and verify JWT tokens
    public String generateToken(UserDto user) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, List<String>> permissionMap = roleMenuService.getAuthorities(user.getId());
        claims.put("permissions", permissionMap);

        List<String> roles = roleService.getRoleByUserId(user.getId()).stream()
                .map(RoleDto::getCode)
                .toList();
        claims.put("roles", roles);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
        claims.put("user", ObjectMapperUtils.convertToJson(userDto));
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDto.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(nmquan.commonlib.utils.JwtUtils.getSignInKey(SECRET_KEY), SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e) {
            throw new AppException(CommonErrorCode.ERROR);
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String generateTokenVerifyEmail(){
        return UUID.randomUUID().toString();
    }

    public String generateTokenResetPassword(){
        return UUID.randomUUID().toString();
    }
}

