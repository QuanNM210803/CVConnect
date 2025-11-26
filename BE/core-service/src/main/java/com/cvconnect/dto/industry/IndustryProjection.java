package com.cvconnect.dto.industry;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface IndustryProjection extends BaseRepositoryDto {
    String getCode();
    String getName();
    String getDescription();

    Long getOrgId();
}
