package com.cvconnect.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyResponse {
    private Boolean isValid;

    private String message;
    private HttpStatus status;
    private Integer code;
}