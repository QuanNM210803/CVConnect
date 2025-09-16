package com.cvconnect.dto.menu;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface MenuProjection extends BaseRepositoryDto {
    String getMenuCode();
    String getMenuLabel();
    String getMenuIcon();
    String getMenuUrl();
    Long getParentId();
    Integer getMenuSortOrder();
    String getForMemberType();
    Boolean getIsShow();
    String getPermission();
}
