package com.cvconnect.service;

import com.cvconnect.dto.role.RoleDto;

import java.util.List;

public interface RoleService {
    List<String> getAuthorities(Long userId);

    RoleDto getRoleByCode(String code);
}
