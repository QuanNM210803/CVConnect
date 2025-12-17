package com.cvconnect.job.failedRollback;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.dto.failedRollback.FailedRollbackUpdateAccountStatus;
import com.cvconnect.enums.FailedRollbackType;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedRollbackUpdateAccountStatusHandler implements FailedRollbackHandler{
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Override
    public FailedRollbackType getType() {
        return FailedRollbackType.UPDATE_ACCOUNT_STATUS;
    }

    @Override
    public void rollback(FailedRollbackDto dto) {
        FailedRollbackUpdateAccountStatus payload = ObjectMapperUtils.convertToObject(dto.getPayload(), FailedRollbackUpdateAccountStatus.class);
        restTemplateClient.rollbackUpdateAccountStatusByOrgIds(payload);
    }
}
