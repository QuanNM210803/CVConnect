package com.cvconnect.utils;

import com.cvconnect.enums.CoreErrorCode;
import nmquan.commonlib.exception.AppException;
import org.springframework.web.multipart.MultipartFile;

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
}
