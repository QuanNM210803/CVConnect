package com.cvconnect.service.impl;

import com.cvconnect.dto.menu.MenuDto;
import com.cvconnect.dto.menu.MenuMetadata;
import com.cvconnect.dto.menu.MenuProjection;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.enums.MemberType;
import com.cvconnect.repository.MenuRepository;
import com.cvconnect.service.MenuService;
import com.cvconnect.service.RoleUserService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleUserService roleUserService;

    @Override
    public List<MenuMetadata> getMenusByRoleId(Long roleId) {
        Long userId = WebUtils.getCurrentUserId();
        RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(userId, roleId);
        if (roleUserDto == null) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        List<MenuProjection> projections = menuRepository.findMenusByRoleId(roleId);
        return this.buildMenuTree(projections, true);
    }

    @Override
    public List<MenuMetadata> getAllMenus(MemberType memberType) {
        List<MenuProjection> projections = menuRepository.findAllMenu(memberType);
        return this.buildMenuTree(projections, false);
    }

    @Override
    public List<MenuDto> getMenuByIds(List<Long> ids) {
        List<MenuProjection> projections = menuRepository.findByIds(ids);
        return this.buildMenuDtos(projections);
    }

    public List<MenuMetadata> buildMenuTree(List<MenuProjection> projections, boolean includePermissions) {
        Map<Long, MenuMetadata> menuMap = new HashMap<>();
        for (MenuProjection p : projections) {
            if(!p.getIsShow()){
                continue;
            }
            MenuMetadata menu = new MenuMetadata();
            menu.setId(p.getId());
            menu.setMenuCode(p.getMenuCode());
            menu.setMenuLabel(p.getMenuLabel());
            menu.setMenuIcon(p.getMenuIcon());
            menu.setMenuUrl(p.getMenuUrl());
            menu.setParentId(p.getParentId());
            menu.setMenuSortOrder(p.getMenuSortOrder());
            menu.setForMemberType(p.getForMemberType());
            if(includePermissions && p.getPermission() != null) {
                menu.setPermissions(List.of(p.getPermission().split(",")));
            }
            menuMap.put(menu.getId(), menu);
        }

        List<MenuMetadata> roots = new ArrayList<>();
        for (MenuMetadata menu : menuMap.values()) {
            if (menu.getParentId() == null) {
                roots.add(menu);
            } else {
                MenuMetadata parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                }
            }
        }

        roots.sort(Comparator.comparing(MenuMetadata::getMenuSortOrder)
                .thenComparing(MenuMetadata::getId)
        );
        for (MenuMetadata root : roots) {
            sortChildren(root);
        }

        return roots;
    }

    private void sortChildren(MenuMetadata menu) {
        menu.getChildren().sort(Comparator.comparing(MenuMetadata::getMenuSortOrder)
                .thenComparing(MenuMetadata::getId)
        );
        for (MenuMetadata child : menu.getChildren()) {
            sortChildren(child);
        }
    }

    private List<MenuDto> buildMenuDtos(List<MenuProjection> projections) {
        return projections.stream()
                .map(projection -> MenuDto.builder()
                        .id(projection.getId())
                        .code(projection.getMenuCode())
                        .label(projection.getMenuLabel())
                        .icon(projection.getMenuIcon())
                        .parentId(projection.getParentId())
                        .sortOrder(projection.getMenuSortOrder())
                        .isShow(projection.getIsShow())
                        .forMemberType(projection.getForMemberType())
                        .build())
                .toList();
    }
}
