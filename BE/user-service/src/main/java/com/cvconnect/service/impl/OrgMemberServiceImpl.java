package com.cvconnect.service.impl;
import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.InviteUserRequest;
import com.cvconnect.dto.internal.response.OrgDto;
import com.cvconnect.dto.inviteJoinOrg.InviteJoinOrgDto;
import com.cvconnect.dto.inviteJoinOrg.ReplyInviteUserRequest;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.roleUser.RoleUserDto;
import com.cvconnect.dto.user.UserDto;
import com.cvconnect.entity.OrgMember;
import com.cvconnect.enums.InviteJoinStatus;
import com.cvconnect.enums.MemberType;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.repository.OrgMemberRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.JwtUtils;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.enums.EmailTemplateEnum;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.service.SendEmailService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Value("${frontend.url-invite-join-org}")
    private String URL_INVITE_JOIN_ORG;

    @Override
    public OrgMemberDto getOrgMember(Long userId) {
        Optional<OrgMember> orgMember = orgMemberRepository.findByUserId(userId);
        OrgMember entity = orgMember.orElse(null);
        if(entity == null) {
            return null;
        }
        return OrgMemberDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .orgId(entity.getOrgId())
                .build();
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
        Long orgId = WebUtils.checkCurrentOrgId();
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
                "acceptUrl", URL_INVITE_JOIN_ORG + "?token=" + inviteJoinOrgDto.getToken() + "&action=a",
                "rejectUrl", URL_INVITE_JOIN_ORG + "?token=" + inviteJoinOrgDto.getToken() + "&action=r",
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
        orgMemberRepository.save(newOrgMember);

        RoleUserDto roleUserDto = RoleUserDto.builder()
                .roleId(inviteJoinOrgDto.getRoleId())
                .userId(inviteJoinOrgDto.getUserId())
                .build();
        roleUserService.createRoleUser(roleUserDto);

        // TODO: send notification to org-admin
    }
}
