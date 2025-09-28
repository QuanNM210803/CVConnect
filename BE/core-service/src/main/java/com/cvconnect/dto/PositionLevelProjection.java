package com.cvconnect.dto;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface PositionLevelProjection extends BaseRepositoryDto {
    String getName();
    Long getPositionId();
    Long getLevelId();
    String getLevelName();
    String getLevelCode();
}
