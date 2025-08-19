package com.cvconnect.service.impl;

import com.cvconnect.repository.RoleRepository;
import com.cvconnect.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<String> getAuthorities(Long userId) {
        return List.of();
    }
}
