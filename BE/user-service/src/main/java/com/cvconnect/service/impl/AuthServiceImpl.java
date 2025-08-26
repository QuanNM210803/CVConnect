package com.cvconnect.service.impl;

import com.cvconnect.config.security.JwtUtils;
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
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.exception.ErrorCode;
import nmquan.commonlib.model.JwtUser;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LocalizationUtils localizationUtils;

    @Value("${jwt.refresh-expiration}")
    private int JWT_REFRESHABLE_DURATION;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        UserDto user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new AppException(UserErrorCode.LOGIN_FAIL);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AppException(UserErrorCode.LOGIN_FAIL);
        }

        List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(user.getId());
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtUtils.generateToken(user))
                .roles(roleUserDtos.stream().map(RoleUserDto::getRole).collect(Collectors.toList()))
                .build();

        String refreshToken = jwtUtils.generateRefreshToken();
        TokenInfo tokenInfo = TokenInfo.builder()
                .userId(user.getId())
                .type(TokenType.REFRESH)
                .build();
        redisUtils.saveObject(refreshToken, tokenInfo, JWT_REFRESHABLE_DURATION);
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

        String refreshTokenKey = redisUtils.getFreshTokenKey(rfToken);
        Object raw = redisUtils.getObjectByKey(refreshTokenKey);
        if (raw == null) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        redisUtils.deleteByKey(refreshTokenKey);

        TokenInfo tokenInfo = ObjectMapperUtils.convertToObject(raw.toString(), TokenInfo.class);
        if (tokenInfo == null || !TokenType.REFRESH.equals(tokenInfo.getType())) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }

        UserDto user = userService.findById(tokenInfo.getUserId());
        if (user == null) {
            CookieUtils.deleteRefreshTokenCookie(httpServletResponse);
            throw new AppException(CommonErrorCode.UNAUTHENTICATED);
        }
        String newRefreshToken = jwtUtils.generateRefreshToken();
        redisUtils.saveObject(newRefreshToken, tokenInfo, JWT_REFRESHABLE_DURATION);
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
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .accessMethod(AccessMethod.LOCAL.name())
                    .isEmailVerified(false)
                    .build();
            userDto = userService.create(userDto);

            RoleUserDto roleUserDto = RoleUserDto.builder()
                    .userId(userDto.getId())
                    .roleId(roleCandidate.getId())
                    .build();
            roleUserService.createRoleUser(roleUserDto);

            CandidateDto candidateDto = CandidateDto.builder()
                    .userId(userDto.getId())
                    .build();
            candidateService.createCandidate(candidateDto);
            // send email require verification

            return RegisterCandidateResponse.builder()
                    .id(userDto.getId())
                    .needVerifyEmail(true)
                    .build();
        } else {
            List<RoleUserDto> roleUserDtos = roleUserService.findByUserId(existsByEmail.getId());
            /*
            * Only update when there is only 1 role CANDIDATE and there is no LOCAL access method
            * */
            boolean hasCandidateRole = roleUserDtos.stream()
                    .anyMatch(r -> roleCandidate.getId().equals(r.getRoleId()));
            if (!hasCandidateRole) {
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            if (roleUserDtos.size() > 1) {
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }

            List<String> providers = new ArrayList<>(Arrays.asList(existsByEmail.getAccessMethod().split(",")));
            if (providers.contains(AccessMethod.LOCAL.name())) {
                throw new AppException(UserErrorCode.EMAIL_EXISTS);
            }
            providers.add(AccessMethod.LOCAL.name());
            existsByEmail.setUsername(request.getUsername());
            existsByEmail.setPassword(passwordEncoder.encode(request.getPassword()));
            existsByEmail.setFullName(request.getFullName());
            existsByEmail.setAccessMethod(String.join(",", providers));
            userService.create(existsByEmail);
            return RegisterCandidateResponse.builder()
                    .id(existsByEmail.getId())
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
            if (Boolean.FALSE.equals(userDto.getIsEmailVerified())) {
                return this.buildErrorResponse(UserErrorCode.EMAIL_NOT_VERIFIED);
            }
            if (Boolean.FALSE.equals(userDto.getIsActive())) {
                return this.buildErrorResponse(UserErrorCode.ACCOUNT_NOT_ACTIVE);
            }
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

    private VerifyResponse buildErrorResponse(ErrorCode errorCode, Object... params) {
        return VerifyResponse.builder()
                .isValid(false)
                .message(localizationUtils.getLocalizedMessage(errorCode.getMessage(), params))
                .status(errorCode.getStatusCode())
                .code(errorCode.getCode())
                .build();
    }
}
