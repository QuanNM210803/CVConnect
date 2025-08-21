package com.cvconnect.service;

import java.util.List;

public interface RoleMenuService {
    List<String> getAuthorities(Long userId);
}
