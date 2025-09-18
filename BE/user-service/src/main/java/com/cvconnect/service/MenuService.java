package com.cvconnect.service;

import com.cvconnect.dto.menu.MenuDto;
import com.cvconnect.dto.menu.MenuMetadata;
import com.cvconnect.enums.MemberType;

import java.util.List;

public interface MenuService {
    List<MenuMetadata> getMenusByRoleId(Long roleId);
    List<MenuMetadata> getAllMenus(MemberType memberType);
    List<MenuDto> getMenuByIds(List<Long> ids);
}
