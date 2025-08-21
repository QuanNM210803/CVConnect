package com.cvconnect.config.security;

import com.cvconnect.entity.User;
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

    @Value("${jwt.refresh-secret-key}")
    private String SECRET_KEY_REFRESH;

    @Value("${jwt.refresh-expiration}")
    private int JWT_REFRESHABLE_DURATION;

    private final RoleMenuService roleMenuService;

    // functions to generate and verify JWT tokens
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = roleMenuService.getAuthorities(user.getId());
        claims.put("roles", roles);

        user.setIsActive(null);
        user.setIsDeleted(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setCreatedBy(null);
        user.setUpdatedBy(null);
        claims.put("user", ObjectMapperUtils.convertToJson(user));
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(nmquan.commonlib.utils.JwtUtils.getSignInKey(SECRET_KEY), SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e) {
            throw new AppException(CommonErrorCode.ERROR);
        }
    }

    // function to generate a refresh token
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESHABLE_DURATION * 1000L))
                    .signWith(nmquan.commonlib.utils.JwtUtils.getSignInKey(SECRET_KEY_REFRESH), SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e) {
            throw new AppException(CommonErrorCode.ERROR);
        }
    }
}

