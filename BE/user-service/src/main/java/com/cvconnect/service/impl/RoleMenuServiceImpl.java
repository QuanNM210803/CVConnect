package com.cvconnect.service.impl;

import com.cvconnect.dto.roleMenu.PermissionTypeDto;
import com.cvconnect.dto.roleMenu.RoleMenuDto;
import com.cvconnect.dto.roleMenu.RoleMenuProjection;
import com.cvconnect.entity.RoleMenu;
import com.cvconnect.enums.PermissionType;
import com.cvconnect.repository.RoleMenuRepository;
import com.cvconnect.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Override
    public Map<String, List<String>> getAuthorities(Long userId) {
        List<RoleMenuProjection> roleMenuProjections = roleMenuRepository.findAuthoritiesByUserId(userId);
        return roleMenuProjections.stream()
                .flatMap(object -> {
                    String menuCode = object.getMenuCode();
                    String permission = object.getPermission();
                    if (permission != null && !permission.isEmpty()) {
                        return Arrays.stream(permission.split(","))
                                .map(per -> Map.entry(menuCode, per.trim()));
                    }
                    return Stream.empty();
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    @Override
    public List<PermissionTypeDto> getAllPermissionTypes() {
        return Arrays.stream(PermissionType.values())
                .map(type -> PermissionTypeDto.builder()
                        .code(type.name())
                        .build())
                .toList();
    }

    @Override
    public void saveAll(List<RoleMenuDto> roleMenuDtos) {
        List<RoleMenu> roleMenus = roleMenuDtos.stream()
                .map(roleMenuDto -> {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setId(roleMenuDto.getId());
                    roleMenu.setRoleId(roleMenuDto.getRoleId());
                    roleMenu.setMenuId(roleMenuDto.getMenuId());
                    if(roleMenuDto.getPermissions() != null){
                        List<String> permissions = roleMenuDto.getPermissions().stream()
                                .map(Enum::name)
                                .toList();
                        roleMenu.setPermission(String.join(",", permissions));
                    }
                    return roleMenu;
                })
                .toList();
        roleMenuRepository.saveAll(roleMenus);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        roleMenuRepository.deleteByRoleId(roleId);
    }
}
