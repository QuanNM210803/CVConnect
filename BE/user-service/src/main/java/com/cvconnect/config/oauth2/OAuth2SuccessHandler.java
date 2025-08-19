package com.cvconnect.config.oauth2;

import com.cvconnect.config.security.JwtUtils;
import com.cvconnect.entity.User;
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

    @Value("${frontend.url-candidate}")
    private String frontendUrlCandidate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String token = jwtUtils.generateToken(user);
        getRedirectStrategy().sendRedirect(request, response, frontendUrlCandidate + "/login-success?token=" + token);
    }
}