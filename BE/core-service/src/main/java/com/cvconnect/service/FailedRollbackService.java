package com.cvconnect.service;

import com.cvconnect.dto.failedRollback.FailedRollbackDto;

import java.util.List;

public interface FailedRollbackService {
    void save(FailedRollbackDto failedRollbackDto);
    List<FailedRollbackDto> getPendingFailedRollbacks();
}
