package com.cvconnect.service;

import java.util.List;
import java.util.Map;

public interface RoleMenuService {
    Map<String, List<String>> getAuthorities(Long userId);
}
