package com.cvconnect.service.impl;

import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.enums.EmailTemplateEnum;
import com.cvconnect.utils.JwtUtils;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.*;
import com.cvconnect.dto.auth.*;
import com.cvconnect.dto.candidate.CandidateDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.enums.AccessMethod;
import com.cvconnect.enums.TokenType;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.service.*;
import com.cvconnect.utils.CookieUtils;
import com.cvconnect.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nmquan.commonlib.dto.request.ObjectAndFileRequest;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.exception.ErrorCode;
import nmquan.commonlib.model.JwtUser;
import nmquan.commonlib.service.RestTemplateService;
import nmquan.commonlib.utils.LocalizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private LocalizationUtils localizationUtils;
    @Autowired
    private RedisUtils redis;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplateService restTemplateService;
    @Autowired
    private OrgMemberService orgMemberService;

    @Value("${jwt.refresh-expiration}")
    private int JWT_REFRESHABLE_DURATION;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${frontend.url-verify-email}")
    private String URL_VERIFY_EMAIL;
    @Value("${frontend.url-reset-password}")
    private String URL_RESET_PASSWORD;
    @Value("${jwt.verify-expiration}")
    private int JWT_VERIFY_EMAIL_DURATION;
    @Value("${jwt.reset-password-expiration}")
    private int JWT_RESET_PASSWORD_DURATION;
    @Value("${server.core_service}")
    private String SERVER_CORE_SERVICE;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        UserDto user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new AppException(UserErrorCode.LOGIN_FAIL);
        }
        this.checkAccountStatus(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AppException(UserErrorCode.LOGIN_FAIL);
        }

        List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(user.getId());
        OrgMemberDto orgMemberDto = orgMemberService.getOrgMember(user.getId());
        if(Objects.nonNull(orgMemberDto)){
            user.setOrgId(orgMemberDto.getOrgId());
        }
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtUtils.generateToken(user))
                .roles(roleUserDtos.stream().map(RoleUserDto::getRole).collect(Collectors.toList()))
                .build();

        String refreshToken = jwtUtils.generateRefreshToken();
        this.saveToken(user.getId(), TokenType.REFRESH, refreshToken, JWT_REFRESHABLE_DURATION);
        CookieUtils.setRefreshTokenCookie(refreshToken, JWT_REFRESHABLE_DURATION, httpServletResponse);

        return loginResponse;
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String rfToken = CookieUtils.getRefreshTokenCookie(httpServletRequest);
        if (rfToken == null) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        String refreshTokenKey = redisUtils.getTokenKey(rfToken);
        TokenInfo tokenInfo = redisUtils.getObject(refreshTokenKey, TokenInfo.class);
        if (tokenInfo == null) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        redisUtils.deleteByKey(refreshTokenKey);

        if (!TokenType.REFRESH.equals(tokenInfo.getType())) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        UserDto user = userService.findById(tokenInfo.getUserId());
        if (user == null) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        this.checkAccountStatus(user);
        String newRefreshToken = jwtUtils.generateRefreshToken();
        redisUtils.saveObject(redisUtils.getTokenKey(newRefreshToken), tokenInfo, JWT_REFRESHABLE_DURATION);
        CookieUtils.setRefreshTokenCookie(newRefreshToken, JWT_REFRESHABLE_DURATION, httpServletResponse);

        List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(user.getId());
        return RefreshTokenResponse.builder()
                .token(jwtUtils.generateToken(user))
                .roles(roleUserDtos.stream().map(RoleUserDto::getRole).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String rfToken = CookieUtils.getRefreshTokenCookie(httpServletRequest);
        CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
        if (rfToken != null) {
            redisUtils.deleteByKey(rfToken);
        }
    }

    @Transactional
    @Override
    public RegisterCandidateResponse registerCandidate(RegisterCandidateRequest request) {
        UserDto existsByUsername = userService.findByUsername(request.getUsername());
        if (existsByUsername != null) {
            throw new AppException(UserErrorCode.USERNAME_EXISTS);
        }

        UserDto existsByEmail = userService.findByEmail(request.getEmail());
        RoleDto roleCandidate = roleService.getRoleByCode(Constants.RoleCode.CANDIDATE);
        if(roleCandidate == null) {
            throw new AppException(CommonErrorCode.ERROR);
        }

        if(existsByEmail == null) {
            UserDto userDto = UserDto.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .accessMethod(AccessMethod.LOCAL.name())
                    .isEmailVerified(false)
                    .build();
            userDto = userService.create(userDto);
            this.createCandidateForUser(userDto.getId(), roleCandidate.getId());

            // send email require verification
            String token = jwtUtils.generateTokenVerifyEmail();
            Map<String, String> dataPlaceHolders = new HashMap<>();
            dataPlaceHolders.put("username", userDto.getFullName());
            dataPlaceHolders.put("verifyUrl", URL_VERIFY_EMAIL + "?token=" + token);
            dataPlaceHolders.put("year", String.valueOf(LocalDate.now().getYear()));
            sendEmailService.sendEmailWithTemplate(
                    List.of(userDto.getEmail()),
                    null,
                    EmailTemplateEnum.VERIFY_EMAIL,
                    dataPlaceHolders
            );
            this.saveToken(userDto.getId(), TokenType.VERIFY_EMAIL, token, JWT_VERIFY_EMAIL_DURATION);

            return RegisterCandidateResponse.builder()
                    .id(userDto.getId())
                    .username(userDto.getUsername())
                    .needVerifyEmail(true)
                    .duration((long) JWT_VERIFY_EMAIL_DURATION)
                    .build();
        } else {
            /*
            * Only update when created from OAuth2 and not have password
            * */
            if(existsByEmail.getPassword() != null){
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            List<String> providers = new ArrayList<>(Arrays.asList(existsByEmail.getAccessMethod().split(",")));
            if (providers.contains(AccessMethod.LOCAL.name())) {
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            providers.add(AccessMethod.LOCAL.name());

            RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(existsByEmail.getId(), roleCandidate.getId());
            if (roleUserDto == null) {
                this.createCandidateForUser(existsByEmail.getId(), roleCandidate.getId());
            }

            existsByEmail.setUsername(request.getUsername());
            existsByEmail.setPassword(passwordEncoder.encode(request.getPassword()));
            existsByEmail.setFullName(request.getFullName());
            existsByEmail.setAccessMethod(String.join(",", providers));
            userService.create(existsByEmail);
            return RegisterCandidateResponse.builder()
                    .id(existsByEmail.getId())
                    .username(existsByEmail.getUsername())
                    .needVerifyEmail(false)
                    .build();
        }
    }

    @Override
    @Transactional
    public RegisterOrgAdminResponse registerOrgAdmin(RegisterOrgAdminRequest request, MultipartFile logo, MultipartFile coverPhoto) {
        UserDto existsByUsername = userService.findByUsername(request.getUsername());
        if (existsByUsername != null) {
            throw new AppException(UserErrorCode.USERNAME_EXISTS);
        }

        UserDto existsByEmail = userService.findByEmail(request.getEmail());
        RoleDto roleOrgAdmin = roleService.getRoleByCode(Constants.RoleCode.ORG_ADMIN);
        if(roleOrgAdmin == null) {
            throw new AppException(CommonErrorCode.ERROR);
        }

        if(existsByEmail == null) {
            // create account org-admin
            UserDto userDto = UserDto.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .accessMethod(AccessMethod.LOCAL.name())
                    .isEmailVerified(false)
                    .build();
            userDto = userService.create(userDto);
            OrgMemberDto saved = this.createOrgMemberForUser(userDto.getId(), roleOrgAdmin.getId());

            // create organization
            Response<IDResponse<Long>> orgResponse = this.createOrg(request.getOrganization(), logo, coverPhoto);

            // update orgId for org-admin
            saved.setOrgId(orgResponse.getData().getId());
            orgMemberService.createOrgMember(saved);

            // send email require verification
            String token = jwtUtils.generateTokenVerifyEmail();
            Map<String, String> dataPlaceHolders = new HashMap<>();
            dataPlaceHolders.put("orgName", request.getOrganization().getName());
            dataPlaceHolders.put("verifyUrl", URL_VERIFY_EMAIL + "?token=" + token);
            dataPlaceHolders.put("year", String.valueOf(LocalDate.now().getYear()));
            sendEmailService.sendEmailWithTemplate(
                    List.of(userDto.getEmail()),
                    null,
                    EmailTemplateEnum.VERIFY_ORG_EMAIL,
                    dataPlaceHolders
            );
            this.saveToken(userDto.getId(), TokenType.VERIFY_EMAIL, token, JWT_VERIFY_EMAIL_DURATION);

            return RegisterOrgAdminResponse.builder()
                    .id(userDto.getId())
                    .username(userDto.getUsername())
                    .needVerifyEmail(true)
                    .duration((long) JWT_VERIFY_EMAIL_DURATION)
                    .build();
        } else {
            /*
             * Only update when created from OAuth2 and not have password and not registered as org-member
             * */
            if(existsByEmail.getPassword() != null){
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            List<String> providers = new ArrayList<>(Arrays.asList(existsByEmail.getAccessMethod().split(",")));
            if (providers.contains(AccessMethod.LOCAL.name())) {
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            providers.add(AccessMethod.LOCAL.name());

            boolean existsOrgMember = orgMemberService.existsByUserId(existsByEmail.getId());
            if(existsOrgMember) {
                throw new AppException(UserErrorCode.ACCOUNT_REGISTERED_AS_ORG_MEMBER);
            }

            // create account org-admin
            existsByEmail.setUsername(request.getUsername());
            existsByEmail.setPassword(passwordEncoder.encode(request.getPassword()));
            existsByEmail.setFullName(request.getFullName());
            existsByEmail.setAccessMethod(String.join(",", providers));
            userService.create(existsByEmail);
            OrgMemberDto saved = this.createOrgMemberForUser(existsByEmail.getId(), roleOrgAdmin.getId());

            // create organization
            Response<IDResponse<Long>> orgResponse = this.createOrg(request.getOrganization(), logo, coverPhoto);

            // update orgId for org-admin
            saved.setOrgId(orgResponse.getData().getId());
            orgMemberService.createOrgMember(saved);

            return RegisterOrgAdminResponse.builder()
                    .id(existsByEmail.getId())
                    .username(existsByEmail.getUsername())
                    .needVerifyEmail(false)
                    .build();
        }
    }

    @Override
    public VerifyResponse verify(VerifyRequest verifyRequest) {
        try {
            JwtUser jwtUser = nmquan.commonlib.utils.JwtUtils.validate(verifyRequest.getToken(), SECRET_KEY);
            UserDto userDto = userService.findByUsername(jwtUser.getUsername());
            if(userDto == null) {
                return this.buildErrorResponse(UserErrorCode.USER_NOT_FOUND);
            }
            this.checkAccountStatus(userDto);
            return VerifyResponse.builder()
                    .isValid(true)
                    .status(HttpStatus.OK)
                    .code(1000)
                    .build();
        }catch (AppException e){
            return this.buildErrorResponse(e.getErrorCode(), e.getParams());
        }catch (Exception e) {
            return this.buildErrorResponse(CommonErrorCode.ERROR);
        }
    }

    @Override
    public RequestResendVerifyEmailResponse requestResendVerifyEmail(String identifier) {
        UserDto userDto = userService.findByUsername(identifier);
        if (userDto == null) {
            userDto = userService.findByEmail(identifier);
        }
        if(userDto == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        if(Boolean.TRUE.equals(userDto.getIsEmailVerified())) {
            throw new AppException(UserErrorCode.USER_ALREADY_VERIFIED);
        }
        String token = jwtUtils.generateTokenVerifyEmail();
        Map<String, String> dataPlaceHolders = new HashMap<>();
        dataPlaceHolders.put("username", userDto.getFullName());
        dataPlaceHolders.put("verifyUrl", URL_VERIFY_EMAIL + "?token=" + token);
        dataPlaceHolders.put("year", String.valueOf(LocalDate.now().getYear()));
        sendEmailService.sendEmailWithTemplate(
                List.of(userDto.getEmail()),
                null,
                EmailTemplateEnum.VERIFY_EMAIL,
                dataPlaceHolders
        );
        this.saveToken(userDto.getId(), TokenType.VERIFY_EMAIL, token, JWT_VERIFY_EMAIL_DURATION);
        return RequestResendVerifyEmailResponse.builder()
                .email(userDto.getEmail())
                .duration((long) JWT_VERIFY_EMAIL_DURATION)
                .build();
    }

    @Override
    public VerifyEmailResponse verifyEmail(String token) {
        String tokenKey = redisUtils.getTokenKey(token);
        TokenInfo tokenInfo = redisUtils.getObject(tokenKey, TokenInfo.class);
        if (tokenInfo == null) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        if(!TokenType.VERIFY_EMAIL.equals(tokenInfo.getType())) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        UserDto userDto = userService.findById(tokenInfo.getUserId());
        if (userDto == null) {
            redisUtils.deleteByKey(tokenKey);
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        if(Boolean.TRUE.equals(userDto.getIsEmailVerified())) {
            redisUtils.deleteByKey(tokenKey);
            throw new AppException(UserErrorCode.USER_ALREADY_VERIFIED);
        }
        userService.updateEmailVerified(userDto.getId(), true);
        redisUtils.deleteByKey(tokenKey);
        return VerifyEmailResponse.builder()
                .username(userDto.getUsername())
                .build();
    }

    @Override
    public RequestResetPasswordResponse requestResetPassword(String identifier) {
        UserDto userDto = userService.findByUsername(identifier);
        if (userDto == null) {
            userDto = userService.findByEmail(identifier);
        }
        if(userDto == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        this.checkAccountStatus(userDto);
        // send email reset password
        String token = jwtUtils.generateTokenResetPassword();
        Map<String, String> dataPlaceHolders = new HashMap<>();
        dataPlaceHolders.put("username", userDto.getUsername());
        dataPlaceHolders.put("resetUrl", URL_RESET_PASSWORD + "?token=" + token);
        dataPlaceHolders.put("year", String.valueOf(LocalDate.now().getYear()));
        sendEmailService.sendEmailWithTemplate(
                List.of(userDto.getEmail()),
                null,
                EmailTemplateEnum.RESET_PASSWORD,
                dataPlaceHolders
        );
        this.saveToken(userDto.getId(), TokenType.RESET_PASSWORD, token, JWT_RESET_PASSWORD_DURATION);
        return RequestResetPasswordResponse.builder()
                .email(userDto.getEmail())
                .duration((long) JWT_RESET_PASSWORD_DURATION)
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        String tokenKey = redisUtils.getTokenKey(request.getToken());
        TokenInfo tokenInfo = redisUtils.getObject(tokenKey, TokenInfo.class);
        if (tokenInfo == null) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        if(!TokenType.RESET_PASSWORD.equals(tokenInfo.getType())) {
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        UserDto userDto = userService.findById(tokenInfo.getUserId());
        if (userDto == null) {
            redisUtils.deleteByKey(tokenKey);
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        this.checkAccountStatus(userDto);
        userService.updatePassword(userDto.getId(), request.getNewPassword());
        redisUtils.deleteByKey(tokenKey);
        return ResetPasswordResponse.builder()
                .username(userDto.getUsername())
                .build();
    }

    private VerifyResponse buildErrorResponse(ErrorCode errorCode, Object... params) {
        return VerifyResponse.builder()
                .isValid(false)
                .message(localizationUtils.getLocalizedMessage(errorCode.getMessage(), params))
                .status(errorCode.getStatusCode())
                .code(errorCode.getCode())
                .build();
    }

    private void saveToken(Long userId, TokenType tokenType, String token, int duration){
        TokenInfo tokenInfo = TokenInfo.builder()
                .userId(userId)
                .type(tokenType)
                .build();
        redis.saveObject(redis.getTokenKey(token), tokenInfo, duration);
    }

    private void checkAccountStatus(UserDto userDto){
        if (Boolean.FALSE.equals(userDto.getIsEmailVerified())) {
            throw new AppException(UserErrorCode.EMAIL_NOT_VERIFIED);
        }
        if (Boolean.FALSE.equals(userDto.getIsActive())) {
            throw new AppException(UserErrorCode.ACCOUNT_NOT_ACTIVE);
        }
    }

    /**
     * InternalRequest: core-service to create organization
     * */
    private Response<IDResponse<Long>> createOrg(OrganizationRequest orgRequest, MultipartFile logo, MultipartFile coverPhoto){
        MultipartFile[] files = new MultipartFile[]{logo};
        if(coverPhoto != null) {
            files = new MultipartFile[]{logo, coverPhoto};
        }
        ObjectAndFileRequest<OrganizationRequest> request = ObjectAndFileRequest.<OrganizationRequest>builder()
                .data(orgRequest)
                .files(files)
                .build();
        return restTemplateService.uploadFilesWithObject(
                SERVER_CORE_SERVICE + "/org/internal/create",
                new ParameterizedTypeReference<Response<IDResponse<Long>>>() {},
                request
        );
    }

    private OrgMemberDto createOrgMemberForUser(Long userId, Long roleSystemAdminId){
        RoleUserDto roleUserDto = RoleUserDto.builder()
                .userId(userId)
                .roleId(roleSystemAdminId)
                .build();
        roleUserService.createRoleUser(roleUserDto);

        OrgMemberDto orgMemberDto = OrgMemberDto.builder()
                .userId(userId)
                .orgId(0L)
                .build();
        return orgMemberService.createOrgMember(orgMemberDto);
    }

    private void createCandidateForUser(Long userId, Long roleCandidateId){
        RoleUserDto roleUserDto = RoleUserDto.builder()
                .userId(userId)
                .roleId(roleCandidateId)
                .build();
        roleUserService.createRoleUser(roleUserDto);

        CandidateDto candidateDto = CandidateDto.builder()
                .userId(userId)
                .build();
        candidateService.createCandidate(candidateDto);
    }
}
