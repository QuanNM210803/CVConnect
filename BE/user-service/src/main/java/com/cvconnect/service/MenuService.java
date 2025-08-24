package com.cvconnect.service;

import com.cvconnect.dto.menu.MenuMetadata;

import java.util.List;

public interface MenuService {
    List<MenuMetadata> getMenusByRoleId(Long roleId);
}
