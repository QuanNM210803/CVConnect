package com.cvconnect.dto.org;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface OrgAddressProjection extends BaseRepositoryDto {
    Long getOrgId();
    Boolean getIsHeadquarter();
    String getProvince();
    String getDistrict();
    String getWard();
    String getDetailAddress();
    Long getJobAdId();
}
