package com.cvconnect.service;

import java.util.List;

public interface RoleService {
    List<String> getAuthorities(Long userId);
}
