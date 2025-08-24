package com.cvconnect.service.impl;

import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.roleUser.RoleUserProjection;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.entity.RoleUser;
import com.cvconnect.repository.RoleUserRepository;
import com.cvconnect.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleUserServiceImpl implements RoleUserService {
    @Autowired
    private RoleUserRepository roleUserRepository;
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
    public List<RoleUserDto> findByUserId(Long userId) {
        List<RoleUserProjection> roleUsers = roleUserRepository.findByUserId(userId);
        return roleUsers.stream()
                .map(projection -> RoleUserDto.builder()
                        .id(projection.getId())
                        .role(RoleDto.builder()
                                .id(projection.getRoleId())
                                .code(projection.getRoleCode())
                                .name(projection.getRoleName())
                                .build())
                        .build()
                ).toList();
    }
}
