package com.cvconnect.service.impl;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.entity.Role;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.RoleRepository;
import com.cvconnect.service.RoleService;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDto getRoleByCode(String code) {
        Role role = roleRepository.findByCode(code);
        if(role == null) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND, code);
        }
        return RoleDto.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .memberType(role.getMemberType())
                .build();
    }
}
