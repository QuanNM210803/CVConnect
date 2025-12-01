package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.internal.response.AttachFileDto;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.user.*;
import com.cvconnect.entity.Candidate;
import com.cvconnect.entity.ManagementMember;
import com.cvconnect.entity.OrgMember;
import com.cvconnect.entity.User;
import com.cvconnect.enums.AccessMethod;
import com.cvconnect.enums.MemberType;
import com.cvconnect.enums.TemplateExport;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.ServiceUtils;
import com.cvconnect.utils.UserServiceUtils;
import jakarta.annotation.PostConstruct;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.ExportResponse;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.*;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private RestTemplateClient restTemplateClient;
    @Autowired
    private ServiceUtils serviceUtils;
    @Lazy
    @Autowired
    private AuthService authService;

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
                () -> new AppException(CommonErrorCode.DATA_NOT_FOUND)
        );
        UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);
        UserDetailDto<?> userDetailDto = this.getUserDetail(userId, roleId);
        userDto.setUserDetails(userDetailDto != null ? List.of(userDetailDto) : null);
        if(userDto.getAvatarId() != null){
            AttachFileDto attachFileDto = restTemplateClient.getAttachFileById(userDto.getAvatarId());
            userDto.setAvatarUrl(attachFileDto.getSecureUrl());
        }
        List<RoleUserDto> roleUserDtos = roleUserService.findRoleUseByUserId(userId);
        userDto.setRoles(roleUserDtos.stream().map(RoleUserDto::getRole).collect(Collectors.toList()));
        return userDto.configResponse();
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        List<String> accessMethods = user.getAccessMethod() != null
                ? Arrays.asList(user.getAccessMethod().split(","))
                : Collections.emptyList();
        if (!accessMethods.contains(AccessMethod.LOCAL.name())) {
            throw new AppException(UserErrorCode.REGISTER_THIRD_PARTY);
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
        Long orgId = serviceUtils.validOrgMember();
        List<User> users = userRepository.getUsersByRoleCodeOrg(roleCode, orgId, true);
        if(ObjectUtils.isEmpty(users)){
            return List.of();
        }
        List<UserDto> userDtos = ObjectMapperUtils.convertToList(users, UserDto.class);
        return userDtos.stream()
                .map(UserDto::configResponse)
                .toList();
    }

    @Override
    public List<UserDto> getUsersByRoleCodeOrg(String roleCode, Long orgId) {
        List<User> users = userRepository.getUsersByRoleCodeOrg(roleCode, orgId, true);
        if(ObjectUtils.isEmpty(users)){
            return List.of();
        }
        List<UserDto> userDtos = ObjectMapperUtils.convertToList(users, UserDto.class);
        return userDtos.stream()
                .map(UserDto::configResponse)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);
        return userDto.configResponse();
    }

    @Override
    public void setDefaultRole(Long roleId) {
        Long userId = WebUtils.getCurrentUserId();
        List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(userId);
        if(roleUserDtos.stream().noneMatch(r -> r.getRoleId().equals(roleId))) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND);
        }
        for (RoleUserDto roleUserDto : roleUserDtos) {
            roleUserDto.setIsDefault(roleUserDto.getRoleId().equals(roleId));
        }
        roleUserService.saveList(roleUserDtos);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        List<String> accessMethods = user.getAccessMethod() != null
                ? Arrays.asList(user.getAccessMethod().split(","))
                : Collections.emptyList();
        if (!accessMethods.contains(AccessMethod.LOCAL.name())) {
            throw new AppException(UserErrorCode.REGISTER_THIRD_PARTY);
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(UserErrorCode.CURRENT_PASSWORD_INCORRECT);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        UserServiceUtils.validateImageFileInput(file);
        Long userId = WebUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        Long oldFileId = user.getAvatarId();
        Long fileId = restTemplateClient.uploadFile(new MultipartFile[]{file}).get(0);
        user.setAvatarId(fileId);
        userRepository.save(user);
        if(oldFileId != null) {
            restTemplateClient.deleteAttachFilesByIds(List.of(oldFileId));
        }
    }

    @Override
    @Transactional
    public void updateInfo(UserUpdateRequest request) {
        Long userId = WebUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        User existsEmailUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (!user.getEmail().equals(request.getEmail()) && !ObjectUtils.isEmpty(existsEmailUser)) {
            throw new AppException(UserErrorCode.EMAIL_EXISTS);
        }
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setIsEmailVerified(false);
        userRepository.save(user);

        // send email require verification
        UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);
        authService.sendRequestVerifyEmail(userDto);
    }

    @Override
    public List<RoleDto> getMyRoles() {
        Long userId = WebUtils.getCurrentUserId();
        return roleService.getRoleUseByUserId(userId);
    }

    @Override
    public FilterResponse<UserDto> findNotOrgMember(UserFilterRequest request) {
        request.setMemberTypes(List.of(MemberType.ORGANIZATION));
        Page<User> page = userRepository.findNotOrgMember(request, request.getPageable());
        Page<UserDto> dtoPage = page.map(user -> {
            UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);
            return userDto.configResponse();
        });
        return PageUtils.toFilterResponse(dtoPage, dtoPage.getContent());
    }

    @Override
    public Map<Long, UserDto> getByIds(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        if(ObjectUtils.isEmpty(users)){
            return Map.of();
        }
        List<UserDto> userDtos = ObjectMapperUtils.convertToList(users, UserDto.class);
        return userDtos.stream()
                .map(UserDto::configResponse)
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));
    }

    @Override
    public FilterResponse<UserDto> filter(UserFilterRequest request) {
        if(request.getCreatedAtEnd() != null){
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if(request.getUpdatedAtEnd() != null){
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<UserProjection> page = userRepository.filter(request, request.getPageable());

        List<Long> userIds = page.getContent().stream()
                .map(UserProjection::getId)
                .toList();
        Map<Long, List<RoleDto>> roleUsers =  roleService.getRolesByUserIds(userIds);

        List<UserDto> userDtos = page.getContent().stream()
                .map(p -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(p.getId());
                    userDto.setUsername(p.getUsername());
                    userDto.setEmail(p.getEmail());
                    userDto.setFullName(p.getFullName());
                    userDto.setPhoneNumber(p.getPhoneNumber());
                    userDto.setDateOfBirth(p.getDateOfBirth());
                    userDto.setIsEmailVerified(p.getIsEmailVerified());
                    userDto.setAccessMethod(p.getAccessMethod());
                    if(!ObjectUtils.isEmpty(p.getAccessMethod())){
                        List<String> accessMethod = Arrays.asList(p.getAccessMethod().split(","));
                        List<AccessMethodDto> accessMethodDtos = accessMethod.stream()
                                .map(AccessMethod::getAccessMethodDto)
                                .toList();
                        userDto.setAccessMethods(accessMethodDtos);
                    }
                    if(p.getAvatarId() != null){
                        AttachFileDto avatar = restTemplateClient.getAttachFileById(p.getAvatarId());
                        userDto.setAvatarUrl(avatar.getSecureUrl());
                    }
                    userDto.setRoles(roleUsers.get(p.getId()));
                    userDto.setIsActive(p.getIsActive());
                    userDto.setCreatedAt(p.getCreatedAt());
                    userDto.setUpdatedAt(p.getUpdatedAt());
                    userDto.setCreatedBy(p.getCreatedBy());
                    userDto.setUpdatedBy(p.getUpdatedBy());
                    return userDto.configResponse();
                }).toList();
        return PageUtils.toFilterResponse(page, userDtos);
    }

    @Override
    public UserDto userDetailForSystemAdmin(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        UserDto userDto = ObjectMapperUtils.convertToObject(user, UserDto.class);

        Map<Long, List<RoleDto>> roleUsers =  roleService.getRolesByUserIds(List.of(userId));
        userDto.setRoles(roleUsers.get(userId));

        if(!ObjectUtils.isEmpty(userDto.getAccessMethod())){
            List<String> accessMethod = Arrays.asList(userDto.getAccessMethod().split(","));
            List<AccessMethodDto> accessMethodDtos = accessMethod.stream()
                    .map(AccessMethod::getAccessMethodDto)
                    .toList();
            userDto.setAccessMethods(accessMethodDtos);
        }
        if(userDto.getAvatarId() != null){
            AttachFileDto avatar = restTemplateClient.getAttachFileById(userDto.getAvatarId());
            userDto.setAvatarUrl(avatar.getSecureUrl());
        }

        OrgMemberDto orgMemberDto = orgMemberService.getOrgMemberForSystemAdmin(userId);
        userDto.setOrgMember(orgMemberDto);

        return userDto.configResponse();
    }

    @Override
    @Transactional
    public void assignAdminSystemRole(Long userId) {
        RoleDto roleSystemAdmin = roleService.getRoleByCode(Constants.RoleCode.SYSTEM_ADMIN);
        if(roleSystemAdmin == null) {
            throw new AppException(CommonErrorCode.ERROR);
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        if (Boolean.FALSE.equals(user.getIsEmailVerified())) {
            throw new AppException(UserErrorCode.EMAIL_NOT_VERIFIED);
        }
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppException(UserErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(userId, roleSystemAdmin.getId());
        if(roleUserDto != null) {
            throw new AppException(UserErrorCode.USER_ALREADY_HAS_ROLE);
        }
        roleUserDto = RoleUserDto.builder()
                .userId(userId)
                .roleId(roleSystemAdmin.getId())
                .build();
        roleUserService.createRoleUser(roleUserDto);
    }

    @Override
    @Transactional
    public void retrieveAdminSystemRole(Long userId) {
        Long currentUserId = WebUtils.getCurrentUserId();
        RoleDto roleSystemAdmin = roleService.getRoleByCode(Constants.RoleCode.SYSTEM_ADMIN);
        if(roleSystemAdmin == null) {
            throw new AppException(CommonErrorCode.ERROR);
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(userId, roleSystemAdmin.getId());
        if(roleUserDto == null) {
            throw new AppException(UserErrorCode.USER_DOES_NOT_HAVE_ROLE);
        }
        if(userId.equals(currentUserId)) {
            throw new AppException(UserErrorCode.CANNOT_REMOVE_OWN_SYSTEM_ADMIN_ROLE);
        }
        roleUserService.deleteByUserIdAndRoleIds(userId, List.of(roleUserDto.getId()));

        boolean checkRoleSystemAdminExists = roleUserService.existsUserActiveByRoleId(roleUserDto.getRoleId());
        if(!checkRoleSystemAdminExists) {
            throw new AppException(UserErrorCode.LAST_SYSTEM_ADMIN_CANNOT_BE_REMOVED);
        }
    }

    @Override
    public InputStreamResource exportUser(UserFilterRequest filter) {
        filter.setPageIndex(0);
        filter.setPageSize(Integer.MAX_VALUE);
        FilterResponse<UserDto> filterResponse = this.filter(filter);
        List<UserDto> users = filterResponse.getData();
        users.forEach(u -> {
            if(u.getDateOfBirth() != null) {
                DateFormat dateFormat = new SimpleDateFormat(CommonConstants.DATE_TIME.DD_MM_YYYY_HYPHEN);
                u.setDateOfBirthStr(dateFormat.format(u.getDateOfBirth()));
            }
            if(u.getIsEmailVerified() == true) {
                u.setVerifyEmailStr("Đã xác thực");
            } else {
                u.setVerifyEmailStr("Chưa xác thực");
            }
            if(u.getIsActive() == true) {
                u.setActiveStr("Đang hoạt động");
            } else {
                u.setActiveStr("Ngừng hoạt động");
            }
            if(!ObjectUtils.isEmpty(u.getRoles())) {
                String rolesStr = u.getRoles().stream()
                        .map(RoleDto::getName)
                        .collect(Collectors.joining(", "));
                u.setRolesStr(rolesStr);
            }
            u.setCreatedAtStr(DateUtils.instantToString_HCM(u.getCreatedAt(), CommonConstants.DATE_TIME.DD_MM_YYYY_HH_MM_SS));
            if(!ObjectUtils.isEmpty(u.getUpdatedAt())) {
                u.setUpdatedAtStr(DateUtils.instantToString_HCM(u.getUpdatedAt(), CommonConstants.DATE_TIME.DD_MM_YYYY_HH_MM_SS));
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("data", users);
        map.put("date", DateUtils.instantToString_HCM(Instant.now(), CommonConstants.DATE_TIME.DD_MM_YYYY_HH_MM));
        ByteArrayOutputStream bytes = ExportUtils.genXlsxFromMap(map, TemplateExport.USER_EXPORT_TEMPLATE.getPath());
        return ExportUtils.toInputStreamResource(bytes);
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
