package com.cvconnect.dto;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface PositionProcessProjection extends BaseRepositoryDto {
    String getName();
    Long getPositionId();
    Long getProcessId();
    Integer getSortOrder();
    String getProcessName();
    String getProcessCode();
}
