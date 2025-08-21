package com.cvconnect.service.impl;

import com.cvconnect.dto.user.UserDto;
import com.cvconnect.entity.User;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.UserService;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .build();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(UserErrorCode.LOGIN_FAIL)
        );
    }
}
