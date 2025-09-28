package com.cvconnect.enums;

import com.cvconnect.constant.Messages;
import lombok.Getter;
import nmquan.commonlib.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CoreErrorCode implements ErrorCode {
    UPLOAD_FILE_QUANTITY_EXCEED_LIMIT(3001, Messages.UPLOAD_FILE_QUANTITY_EXCEED_LIMIT, HttpStatus.BAD_REQUEST),
    UPLOAD_FILE_ERROR(3002, Messages.UPLOAD_FILE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_FORMAT_NOT_SUPPORTED(3003, Messages.FILE_FORMAT_NOT_SUPPORTED, HttpStatus.BAD_REQUEST),
    ATTACH_FILE_NOT_FOUND(3004, Messages.ATTACH_FILE_NOT_FOUND, HttpStatus.NOT_FOUND),
    DOWNLOAD_FILE_FAILED(3005, Messages.DOWNLOAD_FILE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR),
    INDUSTRY_NOT_FOUND(3006, Messages.INDUSTRY_NOT_FOUND, HttpStatus.NOT_FOUND),
    PROCESS_TYPE_CANNOT_DELETE_DEFAULT(3007, Messages.PROCESS_TYPE_CANNOT_DELETE_DEFAULT, HttpStatus.BAD_REQUEST),
    PROCESS_TYPE_CODE_DUPLICATED(3008, Messages.PROCESS_TYPE_CODE_DUPLICATED, HttpStatus.BAD_REQUEST),
    PROCESS_TYPE_NOT_FOUND(3009, Messages.PROCESS_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND),
    INDUSTRY_EXCEED_LIMIT(3010, Messages.INDUSTRY_EXCEED_LIMIT, HttpStatus.BAD_REQUEST),
    LEVEL_NOT_FOUND(3011, Messages.LEVEL_NOT_FOUND, HttpStatus.NOT_FOUND),
    LEVEL_CODE_DUPLICATED(3012, Messages.LEVEL_CODE_DUPLICATED, HttpStatus.BAD_REQUEST),
    LEVEL_CANNOT_DELETE_DEFAULT(3013, Messages.LEVEL_CANNOT_DELETE_DEFAULT, HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(3014, Messages.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND),
    DEPARTMENT_CODE_DUPLICATED(3015, Messages.DEPARTMENT_CODE_DUPLICATED, HttpStatus.BAD_REQUEST),
    DEPARTMENT_ORG_ID_REQUIRE(3016, Messages.DEPARTMENT_ORG_ID_REQUIRE, HttpStatus.BAD_REQUEST),
    POSITION_CODE_DUPLICATED(3017, Messages.POSITION_CODE_DUPLICATED, HttpStatus.BAD_REQUEST),
    FIRST_PROCESS_MUST_BE_APPLY(3018, Messages.FIRST_PROCESS_MUST_BE_APPLY, HttpStatus.BAD_REQUEST),
    LAST_PROCESS_MUST_BE_ONBOARD(3019, Messages.LAST_PROCESS_MUST_BE_ONBOARD, HttpStatus.NOT_FOUND),
    POSITION_NOT_FOUND(3020, Messages.POSITION_NOT_FOUND, HttpStatus.NOT_FOUND)
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
