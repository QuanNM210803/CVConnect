package com.cvconnect.job.failedRollback;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.dto.failedRollback.FailedRollbackOrgCreation;
import com.cvconnect.enums.FailedRollbackType;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedRollbackOrgCreationHandler implements FailedRollbackHandler {
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    public FailedRollbackType getType() {
        return FailedRollbackType.ORG_CREATION;
    }

    @Override
    public void rollback(FailedRollbackDto dto) {
        FailedRollbackOrgCreation orgCreation = ObjectMapperUtils.convertToObject(dto, FailedRollbackOrgCreation.class);
        restTemplateClient.deleteOrg(orgCreation);
    }
}
