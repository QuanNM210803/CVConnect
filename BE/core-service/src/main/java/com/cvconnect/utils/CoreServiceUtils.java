package com.cvconnect.utils;

import com.cvconnect.enums.CoreErrorCode;
import nmquan.commonlib.exception.AppException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CoreServiceUtils {

    public static void validateImageFileInput(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            throw new AppException(CoreErrorCode.IMAGE_FILE_INVALID);
        }
    }

    public static void validateDocumentFileInput(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||  contentType.equals("application/msword") || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new AppException(CoreErrorCode.DOCUMENT_FILE_INVALID);
        }
    }

    public static void validateManualEmail(String subject, String template) {
        if (ObjectUtils.isEmpty(subject)) {
            throw new AppException(CoreErrorCode.EMAIL_SUBJECT_REQUIRED);
        }
        if (ObjectUtils.isEmpty(template)) {
            throw new AppException(CoreErrorCode.EMAIL_TEMPLATE_REQUIRED);
        }
    }

    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }

    public static Instant convertLocalDateTimeToInstant(LocalDateTime localDateTime, ZoneId zoneFrom, ZoneId zoneTo) {
        ZonedDateTime fromZonedDateTime = localDateTime.atZone(zoneFrom);
        ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(zoneTo);
        return toZonedDateTime.toInstant();
    }

}
