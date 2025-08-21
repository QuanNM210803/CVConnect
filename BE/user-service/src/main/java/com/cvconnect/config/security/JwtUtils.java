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

    @Value("${jwt.verify-email.expiration}")
    private int EXPIRATION_VERIFY_EMAIL;

    @Value("${jwt.verify-email.secret-key}")
    private String SECRET_KEY_VERIFY_EMAIL;

    private final RoleMenuService roleMenuService;

    // functions to generate and verify JWT tokens
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = roleMenuService.getAuthorities(user.getId());
        claims.put("roles", roles);

        user.setPassword(null);
        claims.put("user", ObjectMapperUtils.convertToJson(user));
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(nmquan.commonlib.utils.JwtUtils.getSignInKey(this.SECRET_KEY), SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e) {
            throw new AppException(CommonErrorCode.ERROR);
        }
    }

    // functions to generate and verify JWT tokens for email verification
    public String generateTokenVerifyEmail(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_VERIFY_EMAIL * 1000L))
                    .signWith(nmquan.commonlib.utils.JwtUtils.getSignInKey(this.SECRET_KEY_VERIFY_EMAIL), SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e) {
            throw new AppException(CommonErrorCode.ERROR);
        }
    }
}

