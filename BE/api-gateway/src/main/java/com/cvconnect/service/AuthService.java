package com.cvconnect.service;

import com.cvconnect.dto.Response;
import com.cvconnect.dto.VerifyRequest;
import com.cvconnect.dto.VerifyResponse;
import com.cvconnect.repository.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClient authClient;

    public Mono<Response<VerifyResponse>> verify(String token) {
        return authClient.verify(VerifyRequest.builder()
                .token(token)
                .build());
    }
}
