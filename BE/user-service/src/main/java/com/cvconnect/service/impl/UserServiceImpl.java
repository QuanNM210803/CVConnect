package com.cvconnect.service.impl;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.user.UserDetailDto;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.entity.Candidate;
import com.cvconnect.entity.ManagementMember;
import com.cvconnect.entity.OrgMember;
import com.cvconnect.entity.User;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.*;
import jakarta.annotation.PostConstruct;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ManagementMemberService managementMemberService;
    @Autowired
    private OrgMemberService orgMemberService;
    @Autowired
    private RoleService roleService;

    private final Map<Class<?>, Function<Long, ?>> fetcherMap = new HashMap<>();

    @PostConstruct
    private void initFetcherMap() {
        fetcherMap.put(ManagementMember.class, managementMemberService::getManagementMember);
        fetcherMap.put(Candidate.class, candidateService::getCandidate);
        fetcherMap.put(OrgMember.class, orgMemberService::getOrgMember);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return this.buildUserDto(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        return this.buildUserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return this.buildUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(this.buildUserEntity(userDto));
        return this.buildUserDto(user);
    }

    @Override
    public UserDto getMyInfo(Long roleId) {
        Long userId = WebUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(CommonErrorCode.UNAUTHENTICATED)
        );
        UserDto userDto = this.buildUserDto(user);
        UserDetailDto<?> userDetailDto = this.getUserDetail(userId, roleId);
        userDto.setUserDetails(userDetailDto != null ? List.of(userDetailDto) : null);
        return userDto;
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateEmailVerified(Long userId, Boolean emailVerified) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        user.setIsEmailVerified(emailVerified);
        userRepository.save(user);
    }

    private UserDto buildUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .accessMethod(user.getAccessMethod())
                .isEmailVerified(user.getIsEmailVerified())
                .isActive(user.getIsActive())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    private User buildUserEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setAddress(userDto.getAddress());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAccessMethod(userDto.getAccessMethod());
        user.setIsEmailVerified(userDto.getIsEmailVerified());

        if (userDto.getIsActive() != null) {
            user.setIsActive(userDto.getIsActive());
        }
        if (userDto.getIsDeleted() != null) {
            user.setIsDeleted(userDto.getIsDeleted());
        }
        user.setCreatedBy(userDto.getCreatedBy());
        user.setUpdatedBy(userDto.getUpdatedBy());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        return user;
    }

    private <T> UserDetailDto<T> getUserDetail(Long userId, Long roleId) {
        RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(userId, roleId);
        if(roleUserDto == null) {
            return null;
        }
        RoleDto roleDto = roleService.getRoleById(roleId);
        Class<?> detailInfoClass = roleDto.getMemberType().getClazz();
        Function<Long, ?> fetcher = fetcherMap.get(detailInfoClass);
        if(fetcher == null) {
            return null;
        }
        T detailInfo = (T) fetcher.apply(userId);
        return UserDetailDto.<T>builder()
                .role(roleDto)
                .detailInfo(detailInfo)
                .build();
    }
}
