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
    ROLE_CODE_EXISTED(1009, Messages.ROLE_CODE_EXISTED, HttpStatus.BAD_REQUEST),
    MENU_NOT_FOUND(1010, Messages.MENU_NOT_FOUND, HttpStatus.NOT_FOUND),
    CANNOT_DELETE_ROLE(1011, Messages.CANNOT_DELETE_ROLE, HttpStatus.BAD_REQUEST),
    USER_ALREADY_VERIFIED(1012, Messages.USER_ALREADY_VERIFIED, HttpStatus.BAD_REQUEST),
    LOGO_REQUIRE(1013, Messages.LOGO_REQUIRE, HttpStatus.BAD_REQUEST),
    ACCOUNT_REGISTERED_AS_ORG_MEMBER(1014, Messages.ACCOUNT_REGISTERED_AS_ORG_MEMBER, HttpStatus.BAD_REQUEST),
    USER_JOINED_ORG(1015, Messages.USER_JOINED_ORG, HttpStatus.BAD_REQUEST),
    USER_BELONG_TO_ANOTHER_ORG(1016, Messages.USER_BELONG_TO_ANOTHER_ORG, HttpStatus.BAD_REQUEST),
    INVITE_NOT_FOUND(1017, Messages.INVITE_NOT_FOUND, HttpStatus.NOT_FOUND),
    STATUS_NOT_BLANK(1018, Messages.STATUS_NOT_BLANK, HttpStatus.BAD_REQUEST),
    ORG_MUST_HAVE_AT_LEAST_ONE_ADMIN(1019, Messages.ORG_MUST_HAVE_AT_LEAST_ONE_ADMIN, HttpStatus.BAD_REQUEST),
    REGISTER_THIRD_PARTY(1020, Messages.REGISTER_THIRD_PARTY, HttpStatus.BAD_REQUEST),
    CURRENT_PASSWORD_INCORRECT(1021, Messages.CURRENT_PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST),
    IMAGE_FILE_INVALID(1022, Messages.IMAGE_FILE_INVALID, HttpStatus.BAD_REQUEST),
    DOCUMENT_FILE_INVALID(1023, Messages.DOCUMENT_FILE_INVALID, HttpStatus.BAD_REQUEST),
    USER_ALREADY_HAS_ROLE(1024, Messages.USER_ALREADY_HAS_ROLE, HttpStatus.BAD_REQUEST),
    USER_DOES_NOT_HAVE_ROLE(1025, Messages.USER_DOES_NOT_HAVE_ROLE, HttpStatus.BAD_REQUEST),
    LAST_SYSTEM_ADMIN_CANNOT_BE_REMOVED(1026, Messages.LAST_SYSTEM_ADMIN_CANNOT_BE_REMOVED, HttpStatus.BAD_REQUEST),
    CANNOT_REMOVE_OWN_SYSTEM_ADMIN_ROLE(1027, Messages.CANNOT_REMOVE_OWN_SYSTEM_ADMIN_ROLE, HttpStatus.BAD_REQUEST)
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
