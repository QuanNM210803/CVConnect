package com.cvconnect.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Objects;

public class CookieUtils {
    private static final String NAME_COOKIE_REFRESH_TOKEN = "refreshToken";

    public static void setRefreshTokenCookie(String refreshToken, int JWT_REFRESHABLE_DURATION, HttpServletResponse httpServletResponse){
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

    public static void deleteRefreshTokenCookie(HttpServletResponse httpServletResponse){
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

    public static String getRefreshTokenCookie(HttpServletRequest httpServletRequest){
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
