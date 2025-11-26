package com.cvconnect.service.impl;

import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.roleUser.RoleUserProjection;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.entity.RoleUser;
import com.cvconnect.enums.MemberType;
import com.cvconnect.repository.RoleUserRepository;
import com.cvconnect.service.OrgMemberService;
import com.cvconnect.service.RoleUserService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleUserServiceImpl implements RoleUserService {
    @Autowired
    private RoleUserRepository roleUserRepository;
    @Lazy
    @Autowired
    private OrgMemberService orgMemberService;

    @Override
    public void createRoleUser(RoleUserDto dto) {
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(dto.getUserId());
        roleUser.setRoleId(dto.getRoleId());
        roleUserRepository.save(roleUser);
    }

    @Override
    public RoleUserDto findByUserIdAndRoleId(Long userId, Long roleId) {
        RoleUser roleUser = roleUserRepository.findByUserIdAndRoleId(userId, roleId);
        if(roleUser == null) {
            return null;
        }
        return RoleUserDto.builder()
                .id(roleUser.getId())
                .userId(roleUser.getUserId())
                .roleId(roleUser.getRoleId())
                .build();
    }

    @Override
    public List<RoleUserDto> findRoleUseByUserId(Long userId) {
        OrgMemberDto orgMember = orgMemberService.getOrgMember(userId);
        List<RoleUserProjection> roleUsers = roleUserRepository.findRoleUseByUserId(List.of(userId));
        return roleUsers.stream()
                .filter(roleUser -> orgMember != null || !MemberType.ORGANIZATION.name().equals(roleUser.getMemberType()))
                .map(projection -> RoleUserDto.builder()
                        .id(projection.getId())
                        .role(RoleDto.builder()
                                .id(projection.getRoleId())
                                .code(projection.getRoleCode())
                                .name(projection.getRoleName())
                                .isDefault(projection.getIsDefault())
                                .build())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<RoleUserDto> findByUserId(Long userId) {
        List<RoleUser> roleUsers = roleUserRepository.findByUserId(userId);
        return ObjectMapperUtils.convertToList(roleUsers, RoleUserDto.class);
    }

    @Override
    public void saveList(List<RoleUserDto> roleUserDtos) {
        List<RoleUser> roleUsers = ObjectMapperUtils.convertToList(roleUserDtos, RoleUser.class);
        roleUserRepository.saveAll(roleUsers);
    }

    @Override
    public List<RoleUserDto> getRolesByUserIds(List<Long> userIds) {
        List<RoleUserProjection> roleUsers = roleUserRepository.findRoleUseByUserId(userIds);
        return roleUsers.stream()
                .map(projection -> RoleUserDto.builder()
                        .id(projection.getId())
                        .userId(projection.getUserId())
                        .roleId(projection.getRoleId())
                        .isDefault(projection.getIsDefault())
                        .role(RoleDto.builder()
                                .id(projection.getRoleId())
                                .code(projection.getRoleCode())
                                .name(projection.getRoleName())
                                .memberType(MemberType.valueOf(projection.getMemberType()))
                                .build())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public void deleteByUserIdAndRoleIds(Long userId, List<Long> roleIds) {
        roleUserRepository.deleteByUserIdAndRoleIds(userId, roleIds);
    }

    @Override
    public boolean existsUserActiveByRoleId(Long roleId) {
        return roleUserRepository.existsUserActiveByRoleId(roleId);
    }
}
