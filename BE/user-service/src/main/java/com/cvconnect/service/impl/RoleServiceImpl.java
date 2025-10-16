package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.menu.MenuDto;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.role.MemberTypeDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.role.RoleFilterRequest;
import com.cvconnect.dto.role.RoleRequest;
import com.cvconnect.dto.roleMenu.RoleMenuDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.entity.Role;
import com.cvconnect.enums.MemberType;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.RoleRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleUserService roleUserService;
    @Lazy
    @Autowired
    private OrgMemberService orgMemberService;

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

        this.saveRoleMenus(role, request.getRoleMenus());

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
        this.saveRoleMenus(role, request.getRoleMenus());

        return IDResponse.<Long>builder()
                .id(role.getId())
                .build();
    }

    @Override
    public FilterResponse<RoleDto> filter(RoleFilterRequest request) {
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
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
        return PageUtils.toFilterResponse(page, data);
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

    @Override
    public List<RoleDto> getRoleUseByUserId(Long userId) {
        OrgMemberDto orgMember = orgMemberService.getOrgMember(userId);
        return roleRepository.getRoleByUserId(userId).stream()
                .filter(role -> orgMember != null || !MemberType.ORGANIZATION.equals(role.getMemberType()))
                .toList();
    }

    @Override
    public RoleDto getDetail(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if(role == null) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND);
        }
        return RoleDto.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .memberTypeDto(MemberTypeDto.builder()
                        .code(role.getMemberType().name())
                        .name(role.getMemberType().getName())
                        .build())
                .roleMenus(roleMenuService.findByRoleId(id))
                .build();
    }

    @Override
    public Map<Long, List<RoleDto>> getRolesByUserIds(List<Long> userIds) {
        List<RoleUserDto> roleUsers = roleUserService.getRolesByUserIds(userIds);
        if (ObjectUtils.isEmpty(roleUsers)) {
            return Map.of();
        }
        return roleUsers.stream()
                .collect(Collectors.groupingBy(
                        RoleUserDto::getUserId,
                        Collectors.mapping(RoleUserDto::getRole, Collectors.toList())
                ));
    }

    @Override
    public List<RoleDto> getRoleByIds(List<Long> ids) {
        if(ObjectUtils.isEmpty(ids)) {
            return List.of();
        }
        List<Role> roles = roleRepository.findAllById(ids);
        if(ObjectUtils.isEmpty(roles)) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(roles, RoleDto.class);
    }

    @Override
    public List<RoleDto> getByMemberType(MemberType memberType) {
        List<Role> roles = roleRepository.findByMemberType(memberType);
        if(ObjectUtils.isEmpty(roles)) {
            return List.of();
        }
        return ObjectMapperUtils.convertToList(roles, RoleDto.class);
    }


    void saveRoleMenus(Role role, List<RoleMenuDto> roleMenus) {
        if(roleMenus != null && !roleMenus.isEmpty()) {
            List<Long> menuIds = roleMenus.stream()
                    .map(RoleMenuDto::getMenuId)
                    .toList();
            if(menuIds.size() != roleMenus.size()) {
                throw new AppException(UserErrorCode.MENU_NOT_FOUND);
            }
            List<MenuDto> menus = menuService.getMenuByIds(menuIds);
            if(menuIds.size() != menus.size()) {
                throw new AppException(UserErrorCode.MENU_NOT_FOUND);
            }
            for (MenuDto menu : menus) {
                String requiredType = menu.getForMemberType();
                if (requiredType != null && !Objects.equals(requiredType, role.getMemberType().name())) {
                    throw new AppException(UserErrorCode.MENU_NOT_FOUND);
                }
            }
            List<RoleMenuDto> roleMenuDtos = roleMenus.stream()
                    .map(rm -> RoleMenuDto.builder()
                            .roleId(role.getId())
                            .menuId(rm.getMenuId())
                            .permissions(rm.getPermissions())
                            .build())
                    .toList();
            roleMenuService.saveAll(roleMenuDtos);
        }
    }
}
