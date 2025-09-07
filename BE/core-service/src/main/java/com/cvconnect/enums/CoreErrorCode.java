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
    INDUSTRY_NOT_FOUND(3006, Messages.INDUSTRY_NOT_FOUND, HttpStatus.NOT_FOUND)
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
