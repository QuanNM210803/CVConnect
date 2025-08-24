package com.cvconnect.config;

import com.cvconnect.dto.Response;
import com.cvconnect.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Filter implements GlobalFilter {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    private final String[] PUBLIC_ENDPOINTS = {
            "/v3/api-docs", "/swagger-ui/", "/swagger-ui.html", "/swagger-ui/index.html",
            "/user/auth/", "/user/oauth2/authorization/google", "/user/login/oauth2/code",
            "/user/menu/menu-by-role"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean isPublicEndpoint = Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(
                endpoint -> exchange.getRequest().getURI().getPath().contains(endpoint)
        );
        if (isPublicEndpoint){
            return chain.filter(exchange);
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isEmpty()) {
            return this.unauthenticated(exchange.getResponse());
        }

        String token = authHeader.get(0).replace("Bearer ", "");
        return authService.verifyToken(token).flatMap(verifyResponse -> {
            if (verifyResponse.getData().getIsValid()){
                return chain.filter(exchange); // dung map thi tra ve Mono<Mono<Void>>
            } else {
                return this.unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> this.unauthenticated(exchange.getResponse()));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        Response<?> apiResponse = Response.builder()
                .code(401)
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }
}
