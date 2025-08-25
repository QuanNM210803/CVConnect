package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import nmquan.commonlib.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
    LOGIN_FAIL(1001, Messages.LOGIN_FAILED, HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(1002, Messages.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1003, Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND),
    EMAIL_NOT_VERIFIED(1004, Messages.EMAIL_NOT_VERIFIED, HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_EXISTS(1005, Messages.USERNAME_NOT_EXISTS, HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(1006, Messages.ACCOUNT_NOT_ACTIVE, HttpStatus.UNAUTHORIZED),
    USERNAME_EXISTS(1007, Messages.USERNAME_EXISTS, HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1008, Messages.EMAIL_EXISTS, HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1009, Messages.TOKEN_INVALID, HttpStatus.UNAUTHORIZED),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus statusCode;

    UserErrorCode(Integer code, String message, HttpStatus statusCode) {
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
