package com.cvconnect.service.impl;

import com.cvconnect.dto.RoleUserDto;
import com.cvconnect.entity.RoleUser;
import com.cvconnect.repository.RoleUserRepository;
import com.cvconnect.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return roleUser;
    }
}
