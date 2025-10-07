package com.cvconnect.dto.positionLevel;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface PositionLevelProjection extends BaseRepositoryDto {
    String getName();
    Long getPositionId();
    Long getLevelId();
    String getLevelName();
    String getLevelCode();
}
