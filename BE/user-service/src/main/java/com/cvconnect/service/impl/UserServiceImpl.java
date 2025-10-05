package com.cvconnect.service.impl;

import com.cvconnect.dto.AttachFileDto;
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
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.service.RestTemplateService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Lazy
    @Autowired
    private OrgMemberService orgMemberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RestTemplateService restTemplateService;

    @Value("${server.core_service}")
    private String SERVER_CORE_SERVICE;

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
        return ObjectMapperUtils.convertToObject(user, UserDto.class);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(user, UserDto.class);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(ObjectMapperUtils.convertToObject(userDto, User.class));
        return ObjectMapperUtils.convertToObject(user, UserDto.class);
    }

    @Override
    public UserDto getMyInfo(Long roleId) {
        Long userId = WebUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(CommonErrorCode.UNAUTHENTICATED)
        );
        UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);
        UserDetailDto<?> userDetailDto = this.getUserDetail(userId, roleId);
        userDto.setUserDetails(userDetailDto != null ? List.of(userDetailDto) : null);
        if(userDto.getAvatarId() != null){
            Response<AttachFileDto> response = restTemplateService.getMethodRestTemplate(
                    SERVER_CORE_SERVICE + "/attach-file/internal/get-by-id/{avatarId}",
                    new ParameterizedTypeReference<Response<AttachFileDto>>() {},
                    userDto.getAvatarId()
            );
            userDto.setAvatarUrl(response.getData().getSecureUrl());
        }
        return userDto.configResponse();
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

    @Override
    public Boolean checkOrgUserRole(Long userId, String roleCode, Long orgId) {
        return userRepository.checkOrgUserRole(userId, roleCode, orgId);
    }

    @Override
    public List<UserDto> getUsersByRoleCodeOrg(String roleCode) {
        Long orgId = WebUtils.checkCurrentOrgId();
        List<User> users = userRepository.getUsersByRoleCodeOrg(roleCode, orgId, true);
        if(ObjectUtils.isEmpty(users)){
            return List.of();
        }
        List<UserDto> userDtos = ObjectMapperUtils.convertToList(users, UserDto.class);
        return userDtos.stream()
                .map(UserDto::configResponse)
                .toList();
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
