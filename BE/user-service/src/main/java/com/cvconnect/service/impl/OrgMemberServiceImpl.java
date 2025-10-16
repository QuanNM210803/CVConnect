package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.common.AssignRoleRequest;
import com.cvconnect.dto.common.InviteUserRequest;
import com.cvconnect.dto.common.NotificationDto;
import com.cvconnect.dto.internal.response.OrgDto;
import com.cvconnect.dto.inviteJoinOrg.InviteJoinOrgDto;
import com.cvconnect.dto.common.ReplyInviteUserRequest;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.orgMember.OrgMemberFilter;
import com.cvconnect.dto.orgMember.OrgMemberProjection;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.entity.OrgMember;
import com.cvconnect.enums.InviteJoinStatus;
import com.cvconnect.enums.MemberType;
import com.cvconnect.enums.NotifyTemplate;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.OrgMemberRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.ServiceUtils;
import com.cvconnect.utils.JwtUtils;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.enums.EmailTemplateEnum;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.service.SendEmailService;
import nmquan.commonlib.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrgMemberServiceImpl implements OrgMemberService {
    @Autowired
    private OrgMemberRepository orgMemberRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private InviteJoinOrgService inviteJoinOrgService;
    @Lazy
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private ServiceUtils serviceUtils;
    @Autowired
    private KafkaUtils kafkaUtils;
    @Autowired
    private LocalizationUtils localizationUtils;
    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @Override
    public OrgMemberDto getOrgMember(Long userId) {
        Optional<OrgMember> orgMember = orgMemberRepository.findByUserId(userId);
        OrgMember entity = orgMember.orElse(null);
        if(entity == null || !entity.getIsActive()) {
            return null;
        }
        OrgDto orgDto = restTemplateClient.getOrgById(entity.getOrgId());
        OrgMemberDto orgMemberDto = ObjectMapperUtils.convertToObject(entity, OrgMemberDto.class);
        orgMemberDto.setOrg(orgDto);
        return orgMemberDto;
    }

    @Override
    public OrgMemberDto createOrgMember(OrgMemberDto orgMemberDto) {
        OrgMember orgMember = ObjectMapperUtils.convertToObject(orgMemberDto, OrgMember.class);
        orgMemberRepository.save(orgMember);
        return ObjectMapperUtils.convertToObject(orgMember, OrgMemberDto.class);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return orgMemberRepository.existsByUserId((userId));
    }

    @Override
    @Transactional
    public void inviteUserToJoinOrg(InviteUserRequest request) {
        Long orgId = serviceUtils.validOrgMember();
        Optional<OrgMember> orgMember = orgMemberRepository.findByUserId(request.getUserId());
        if(orgMember.isPresent()) {
            if(orgMember.get().getOrgId().equals(orgId)) {
                throw new AppException(UserErrorCode.USER_JOINED_ORG);
            } else {
                throw new AppException(UserErrorCode.USER_BELONG_TO_ANOTHER_ORG);
            }
        }
        UserDto userDto = userService.findById(request.getUserId());
        if(userDto == null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        if(!userDto.getIsEmailVerified()){
            throw new AppException(UserErrorCode.EMAIL_NOT_VERIFIED);
        }

        RoleDto roleDto = roleService.getRoleById(request.getRoleId());
        if(roleDto == null || !MemberType.ORGANIZATION.equals(roleDto.getMemberType())) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND);
        }

        InviteJoinOrgDto inviteJoinOrgDto = InviteJoinOrgDto.builder()
                .userId(request.getUserId())
                .roleId(roleDto.getId())
                .orgId(orgId)
                .status(InviteJoinStatus.PENDING.name())
                .token(jwtUtils.generateTokenInviteJoinOrg())
                .build();
        inviteJoinOrgService.create(List.of(inviteJoinOrgDto));

        OrgDto orgDto = restTemplateClient.getOrgById(orgId);
        // send email to user
        Map<String, String> templateVariables = Map.of(
                "fullName", userDto.getFullName(),
                "orgName", orgDto.getName(),
                "roleName", roleDto.getName(),
                "acceptUrl", FRONTEND_URL + Constants.Path.INVITE_JOIN_ORG + "?token=" + inviteJoinOrgDto.getToken() + "&action=a",
                "rejectUrl", FRONTEND_URL + Constants.Path.INVITE_JOIN_ORG + "?token=" + inviteJoinOrgDto.getToken() + "&action=r",
                "year", String.valueOf(LocalDate.now().getYear())
        );
        sendEmailService.sendEmailWithTemplate(List.of(userDto.getEmail()), null, EmailTemplateEnum.INVITE_JOIN_ORG, templateVariables);
    }

    @Override
    @Transactional
    public void replyInviteJoinOrg(ReplyInviteUserRequest request) {
        InviteJoinOrgDto inviteJoinOrgDto = inviteJoinOrgService.findByToken(request.getToken());
        if(inviteJoinOrgDto == null) {
            throw new AppException(UserErrorCode.INVITE_NOT_FOUND);
        }
        InviteJoinStatus inviteJoinStatus = InviteJoinStatus.getInviteJoinStatus(inviteJoinOrgDto.getStatus());
        if(ObjectUtils.isEmpty(inviteJoinStatus) || !InviteJoinStatus.PENDING.equals(inviteJoinStatus)) {
            throw new AppException(UserErrorCode.INVITE_NOT_FOUND);
        }

        InviteJoinStatus replyStatus = request.getStatus();
        if(!InviteJoinStatus.ACCEPTED.equals(replyStatus) && !InviteJoinStatus.REJECTED.equals(replyStatus)) {
            throw new AppException(UserErrorCode.STATUS_NOT_BLANK);
        }

        Optional<OrgMember> orgMember = orgMemberRepository.findByUserId(inviteJoinOrgDto.getUserId());
        if(orgMember.isPresent()) {
            if(orgMember.get().getOrgId().equals(inviteJoinOrgDto.getOrgId())) {
                throw new AppException(UserErrorCode.USER_JOINED_ORG);
            } else {
                throw new AppException(UserErrorCode.USER_BELONG_TO_ANOTHER_ORG);
            }
        }

        inviteJoinOrgDto.setStatus(replyStatus.name());
        inviteJoinOrgService.create(List.of(inviteJoinOrgDto));

        OrgMember newOrgMember = new OrgMember();
        newOrgMember.setOrgId(inviteJoinOrgDto.getOrgId());
        newOrgMember.setUserId(inviteJoinOrgDto.getUserId());
        newOrgMember.setInviter(inviteJoinOrgDto.getCreatedBy());
        orgMemberRepository.save(newOrgMember);

        RoleUserDto roleUserDto = RoleUserDto.builder()
                .roleId(inviteJoinOrgDto.getRoleId())
                .userId(inviteJoinOrgDto.getUserId())
                .build();
        roleUserService.createRoleUser(roleUserDto);

        // TODO: send notification to org-admin
        UserDto userDto = userService.findById(inviteJoinOrgDto.getUserId());
        RoleDto roleDto = roleService.getRoleById(inviteJoinOrgDto.getRoleId());
        List<UserDto> orgAdmin = userService.getUsersByRoleCodeOrg(Constants.RoleCode.ORG_ADMIN, inviteJoinOrgDto.getOrgId());
        NotifyTemplate template = NotifyTemplate.NEW_MEMBER_JOINED_ORG;
        NotificationDto notificationDto = NotificationDto.builder()
                .title(template.getTitle())
                .message(localizationUtils.getLocalizedMessage(template.getMessage(), userDto.getFullName(), roleDto.getName()))
                .type(Constants.NotificationType.USER)
                .redirectUrl(Constants.Path.ORG_MEMBER + "?mode=view&targetId=" + inviteJoinOrgDto.getUserId())
                .senderId(inviteJoinOrgDto.getUserId())
                .receiverIds(orgAdmin.stream().map(UserDto::getId).toList())
                .build();
        kafkaUtils.sendWithJson(Constants.KafkaTopic.NOTIFICATION, notificationDto);
    }

    @Override
    public FilterResponse<OrgMemberDto> filter(OrgMemberFilter request) {
        Long orgId = serviceUtils.validOrgMember();
        request.setOrgId(orgId);
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<OrgMemberProjection> page = orgMemberRepository.filter(request, request.getPageable());
        Page<OrgMemberDto> orgMemberDtoPage = page.map(projection -> OrgMemberDto.builder()
                .userId(projection.getUserId())
                .username(projection.getUsername())
                .email(projection.getEmail())
                .fullName(projection.getFullName())
                .phoneNumber(projection.getPhoneNumber())
                .dateOfBirth(projection.getDateOfBirth())
                .isEmailVerified(projection.getIsEmailVerified())
                .isActive(projection.getIsActive())
                .createdAt(projection.getCreatedAt())
                .updatedAt(projection.getUpdatedAt())
                .inviter(projection.getInviter())
                .updatedBy(projection.getUpdatedBy())
                .build());
        List<OrgMemberDto> orgMemberDtos = orgMemberDtoPage.getContent();
        List<Long> userIds = orgMemberDtos.stream()
                .map(OrgMemberDto::getUserId)
                .toList();
        Map<Long, List<RoleDto>> userRoles = roleService.getRolesByUserIds(userIds);
        orgMemberDtos.forEach(orgMemberDto -> {
            List<RoleDto> roles = userRoles.get(orgMemberDto.getUserId()).stream()
                    .filter(role -> MemberType.ORGANIZATION.equals(role.getMemberType()))
                    .toList();
            orgMemberDto.setRoles(roles);
        });

        return PageUtils.toFilterResponse(orgMemberDtoPage, orgMemberDtos);
    }

    @Override
    public Long validOrgMember() {
        return serviceUtils.validOrgMember();
    }

    @Override
    @Transactional
    public void assignRoleOrgMember(AssignRoleRequest request) {
        Long orgId = serviceUtils.validOrgMember();
        Optional<OrgMember> orgMember = orgMemberRepository.findByUserId(request.getUserId());
        OrgMember entity = orgMember.orElse(null);
        if(entity == null || !entity.getOrgId().equals(orgId)) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }

        List<Long> roleIdsInReq = request.getRoleIds();
        List<RoleDto> roleDtos = roleService.getRoleByIds(roleIdsInReq);
        boolean isValid = roleDtos.stream()
                .allMatch(roleDto -> MemberType.ORGANIZATION.equals(roleDto.getMemberType()));
        if(!isValid || roleDtos.size() != roleIdsInReq.size()) {
            throw new AppException(UserErrorCode.ROLE_NOT_FOUND);
        }

        // add new role
        List<RoleUserDto> currentRoleUsers = roleUserService.findByUserId(request.getUserId());
        List<RoleUserDto> newRoleUsers = roleIdsInReq.stream()
                .filter(roleId -> currentRoleUsers.stream().noneMatch(r -> r.getRoleId().equals(roleId)))
                .map(roleId -> RoleUserDto.builder()
                        .userId(request.getUserId())
                        .roleId(roleId)
                        .build())
                .collect(Collectors.toList());
        roleUserService.saveList(newRoleUsers);

        // delete role
        List<Long> deleteRoleIds = currentRoleUsers.stream()
                .map(RoleUserDto::getRoleId)
                .filter(roleId -> !roleIdsInReq.contains(roleId))
                .toList();
        if(!ObjectUtils.isEmpty(deleteRoleIds)) {
            roleUserService.deleteByUserIdAndRoleIds(request.getUserId(), deleteRoleIds);
        }

        boolean existsOrgAdmin = orgMemberRepository.checkExistsOrgAdmin(orgId, Constants.RoleCode.ORG_ADMIN);
        if(!existsOrgAdmin) {
            throw new AppException(UserErrorCode.ORG_MUST_HAVE_AT_LEAST_ONE_ADMIN);
        }
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        Long orgId = serviceUtils.validOrgMember();
        List<OrgMember> orgMembers = orgMemberRepository.findByIdsAndOrgId(request.getIds(), orgId);
        if (orgMembers.size() != request.getIds().size()) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }
        orgMembers.forEach(orgMember -> orgMember.setIsActive(request.getActive()));
        orgMemberRepository.saveAll(orgMembers);

        boolean existsOrgAdmin = orgMemberRepository.checkExistsOrgAdmin(orgId, Constants.RoleCode.ORG_ADMIN);
        if(!existsOrgAdmin) {
            throw new AppException(UserErrorCode.ORG_MUST_HAVE_AT_LEAST_ONE_ADMIN);
        }
    }
}
