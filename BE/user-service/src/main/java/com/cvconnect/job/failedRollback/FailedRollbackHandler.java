package com.cvconnect.job.failedRollback;

import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.enums.FailedRollbackType;

public interface FailedRollbackHandler {
    FailedRollbackType getType();
    void rollback(FailedRollbackDto dto);
}
