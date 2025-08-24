package com.cvconnect.config.oauth2;

import com.cvconnect.config.security.JwtUtils;
import com.cvconnect.dto.TokenInfo;
import com.cvconnect.entity.User;
import com.cvconnect.enums.TokenType;
import com.cvconnect.utils.CookieUtils;
import com.cvconnect.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;

    @Value("${frontend.url-candidate}")
    private String frontendUrlCandidate;
    @Value("${jwt.refresh-expiration}")
    private int JWT_REFRESHABLE_DURATION;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String refreshToken = jwtUtils.generateRefreshToken();
        String refreshTokenKey = redisUtils.getFreshTokenKey(refreshToken);
        TokenInfo tokenInfo = TokenInfo.builder()
                .userId(user.getId())
                .type(TokenType.REFRESH)
                .build();
        redisUtils.saveObject(refreshTokenKey, tokenInfo, JWT_REFRESHABLE_DURATION);
        CookieUtils.setRefreshTokenCookie(refreshToken, JWT_REFRESHABLE_DURATION, response);

        getRedirectStrategy().sendRedirect(request, response, frontendUrlCandidate);
    }
}