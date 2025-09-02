package com.cvconnect.dto.roleMenu;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface RoleMenuProjection extends BaseRepositoryDto {
    Long getRoleMenuId();
    String getPermission();
    String getMenuCode();
}
