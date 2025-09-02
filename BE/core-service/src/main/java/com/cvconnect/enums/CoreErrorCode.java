package com.cvconnect.enums;

import lombok.Getter;
import nmquan.commonlib.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CoreErrorCode implements ErrorCode {
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus statusCode;

    CoreErrorCode(Integer code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public HttpStatus getStatusCode() {
        return statusCode;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
