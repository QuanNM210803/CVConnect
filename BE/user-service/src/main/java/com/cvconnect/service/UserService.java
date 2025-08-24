package com.cvconnect.service;

import com.cvconnect.dto.user.UserDto;

public interface UserService {
    UserDto findById(Long id);
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    UserDto create(UserDto user);
}
