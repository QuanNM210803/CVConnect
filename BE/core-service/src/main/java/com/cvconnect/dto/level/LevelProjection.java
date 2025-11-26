package com.cvconnect.dto.level;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface LevelProjection extends BaseRepositoryDto {
    String getCode();
    String getName();
    Boolean getIsDefault();
    Long getJobAdId();

}
