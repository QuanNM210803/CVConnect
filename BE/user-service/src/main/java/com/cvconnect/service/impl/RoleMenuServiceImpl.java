package com.cvconnect.service.impl;

import com.cvconnect.dto.RoleMenuProjection;
import com.cvconnect.repository.RoleMenuRepository;
import com.cvconnect.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Override
    public List<String> getAuthorities(Long userId) {
        List<RoleMenuProjection> roleMenuProjections = roleMenuRepository.findAuthoritiesByUserId(userId);
        return roleMenuProjections.stream()
                .map(object -> {
                    String permission = object.getPermission();
                    String menuCode = object.getMenuCode();
                    return permission != null ? permission + ":" + menuCode : menuCode;
                })
                .collect(Collectors.toList());
    }
}
