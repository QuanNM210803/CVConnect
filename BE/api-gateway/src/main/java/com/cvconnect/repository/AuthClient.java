package com.cvconnect.repository;

import com.cvconnect.dto.Response;
import com.cvconnect.dto.VerifyRequest;
import com.cvconnect.dto.VerifyResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {

    @PostExchange(url = "/user/auth/verify-token", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<Response<VerifyResponse>> verifyToken(@RequestBody VerifyRequest verifyRequest);

}
