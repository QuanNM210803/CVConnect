package com.cvconnect.service;

import com.cvconnect.dto.role.RoleDto;

public interface RoleService {
    RoleDto getRoleByCode(String code);
}
