package com.cvconnect.job.failedRollback;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.dto.failedRollback.FailedRollbackUploadFile;
import com.cvconnect.enums.FailedRollbackType;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedRollbackUploadFileHandler implements FailedRollbackHandler {
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    public FailedRollbackType getType() {
        return FailedRollbackType.UPLOAD_FILE;
    }

    @Override
    public void rollback(FailedRollbackDto dto) {
        FailedRollbackUploadFile uploadFile = ObjectMapperUtils.convertToObject(dto, FailedRollbackUploadFile.class);
        restTemplateClient.deleteAttachFilesByIds(uploadFile.getAttachFileIds());
    }
}
