package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import nmquan.commonlib.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum NotifyErrorCode implements ErrorCode {
    EMAIL_CONFIG_NOT_FOUND(2001, Messages.EMAIL_CONFIG_NOT_FOUND, HttpStatus.NOT_FOUND)
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus statusCode;

    NotifyErrorCode(Integer code, String message, HttpStatus statusCode) {
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
