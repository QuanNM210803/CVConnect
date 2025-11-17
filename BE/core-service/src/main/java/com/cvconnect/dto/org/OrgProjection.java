package com.cvconnect.dto.org;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface OrgProjection extends BaseRepositoryDto {
    String getName();
    String getDescription();
    Long getLogoId();
    Long getCoverPhotoId();
    String getWebsite();
    Integer getStaffCountFrom();
    Integer getStaffCountTo();
    Long getNumOfJobAds();
}
