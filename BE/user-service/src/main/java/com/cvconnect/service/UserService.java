package com.cvconnect.service;

import com.cvconnect.dto.user.UserDto;
import com.cvconnect.entity.User;

public interface UserService {
    UserDto findById(Long id);
    User findByUsername(String username);
}
