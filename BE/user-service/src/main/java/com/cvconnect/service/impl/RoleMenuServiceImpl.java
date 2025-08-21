package com.cvconnect.service.impl;

import com.cvconnect.dto.RoleMenuProjection;
import com.cvconnect.repository.RoleMenuRepository;
import com.cvconnect.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Override
    public List<String> getAuthorities(Long userId) {
        List<RoleMenuProjection> roleMenuProjections = roleMenuRepository.findAuthoritiesByUserId(userId);
        return roleMenuProjections.stream()
                .flatMap(object -> {
                    String menuCode = object.getMenuCode();
                    String permission = object.getPermission();
                    if (permission != null && !permission.isEmpty()) {
                        return Arrays.stream(permission.split(","))
                                .map(per -> per + ":" + menuCode);
                    }
                    return Stream.of();
                })
                .toList();
    }

}
