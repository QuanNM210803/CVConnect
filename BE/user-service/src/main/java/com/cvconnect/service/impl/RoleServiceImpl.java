package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.role.MemberTypeDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.role.RoleFilterRequest;
import com.cvconnect.dto.role.RoleRequest;
import com.cvconnect.dto.roleMenu.RoleMenuDto;
import com.cvconnect.entity.Role;
import com.cvconnect.enums.MemberType;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.RoleRepository;
import com.cvconnect.service.MenuService;
import com.cvconnect.service.RoleMenuService;
import com.cvconnect.service.RoleService;
import nmquan.commonlib.dto.PageInfo;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public RoleDto getRoleByCode(String code) {
        Role role = roleRepository.findByCode(code);
        if(role == null) {
            return null;
        }
        return RoleDto.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .memberType(role.getMemberType())
                .build();
    }

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(UserErrorCode.ROLE_NOT_FOUND));
        return RoleDto.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .memberType(role.getMemberType())
                .build();
    }

    @Override
    public List<MemberTypeDto> getAllMemberTypes() {
        return Arrays.stream(MemberType.values())
                .map(mt -> MemberTypeDto.builder()
                        .code(mt.name())
                        .name(mt.getName())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public IDResponse<Long> createRoles(RoleRequest request) {
        Role roleCheckExists = roleRepository.findByCode(request.getCode());
        if(roleCheckExists != null) {
            throw new AppException(UserErrorCode.ROLE_CODE_EXISTED);
        }
        Role role = new Role();
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setMemberType(request.getMemberType());
        roleRepository.save(role);

        this.saveRoleMenus(role.getId(), request.getRoleMenus());

        return IDResponse.<Long>builder()
                .id(role.getId())
                .build();
    }

    @Override
    @Transactional
    public IDResponse<Long> updateRoles(RoleRequest request) {
        Role role = roleRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(UserErrorCode.ROLE_NOT_FOUND));
        Role roleCheckExists = roleRepository.findByCode(request.getCode());
        if(roleCheckExists != null && !Objects.equals(roleCheckExists.getId(), request.getId())) {
            throw new AppException(UserErrorCode.ROLE_CODE_EXISTED);
        }
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setMemberType(request.getMemberType());
        roleRepository.save(role);

        roleMenuService.deleteByRoleId(role.getId());
        this.saveRoleMenus(role.getId(), request.getRoleMenus());

        return IDResponse.<Long>builder()
                .id(role.getId())
                .build();
    }

    @Override
    public FilterResponse<RoleDto> filter(RoleFilterRequest request) {
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(request.getCreatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(request.getUpdatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        Page<RoleDto> page = roleRepository.filter(request, request.getPageable());
        List<String> roleCode = Constants.RoleCode.getAllRoleCodes();
        List<RoleDto> data = page.getContent().stream()
                .map(dto -> {
                    if (roleCode.contains(dto.getCode())) {
                        dto.setCanDelete(false);
                    } else {
                        dto.setCanDelete(true);
                    }
                    MemberTypeDto memberTypeDto = MemberTypeDto.builder()
                            .code(dto.getMemberType().name())
                            .memberType(dto.getMemberType().getName())
                            .build();
                    dto.setMemberTypeDto(memberTypeDto);
                    dto.setMemberType(null);
                    return dto;
                }).toList();

        PageInfo pageInfo = PageInfo.builder()
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .build();
        return FilterResponse.<RoleDto>builder()
                .pageInfo(pageInfo)
                .data(data)
                .build();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        List<Role> roles = roleRepository.findAllById(ids);
        if(roles.size() != ids.size()) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND);
        }
        List<String> roleCode = Constants.RoleCode.getAllRoleCodes();
        roles.forEach(role -> {
            if(roleCode.contains(role.getCode())) {
                throw new AppException(UserErrorCode.CANNOT_DELETE_ROLE);
            }
        });
        roleRepository.deleteByIds(ids);
    }

    @Override
    public List<RoleDto> getRoleByUserId(Long userId) {
        return roleRepository.getRoleByUserId(userId);
    }

    void saveRoleMenus(Long roleId, List<RoleMenuDto> roleMenus) {
        if(roleMenus != null && !roleMenus.isEmpty()) {
            List<Long> menuIds = roleMenus.stream()
                    .map(RoleMenuDto::getMenuId)
                    .toList();
            if(menuIds.size() != roleMenus.size()) {
                throw new AppException(UserErrorCode.MENU_NOT_FOUND);
            }
            if(menuIds.size() != menuService.getMenuByIds(menuIds).size()) {
                throw new AppException(UserErrorCode.MENU_NOT_FOUND);
            }
            List<RoleMenuDto> roleMenuDtos = roleMenus.stream()
                    .map(rm -> RoleMenuDto.builder()
                            .roleId(roleId)
                            .menuId(rm.getMenuId())
                            .permissions(rm.getPermissions())
                            .build())
                    .toList();
            roleMenuService.saveAll(roleMenuDtos);
        }
    }
}
