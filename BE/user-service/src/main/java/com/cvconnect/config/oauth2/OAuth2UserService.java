package com.cvconnect.config.oauth2;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.RoleUserDto;
import com.cvconnect.dto.candidate.CandidateDto;
import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.entity.User;
import com.cvconnect.enums.AccessMethod;
import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.CandidateService;
import com.cvconnect.service.RoleService;
import com.cvconnect.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        RoleDto roleCandidate = roleService.getRoleByCode(Constants.RoleCode.CANDIDATE);

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    String currentAccessMethod = existingUser.getAccessMethod();
                    if (currentAccessMethod == null || currentAccessMethod.isEmpty()) {
                        existingUser.setAccessMethod(AccessMethod.GOOGLE.name());
                    } else {
                        List<String> accessMethods = new ArrayList<>(Arrays.asList(currentAccessMethod.split(",")));
                        if (!accessMethods.contains(AccessMethod.GOOGLE.name())) {
                            accessMethods.add(AccessMethod.GOOGLE.name());
                            existingUser.setAccessMethod(String.join(",", accessMethods));
                        }
                    }
                    RoleUserDto roleUserDto = roleUserService.findByUserIdAndRoleId(existingUser.getId(), roleCandidate.getId());
                    if (roleUserDto == null) {
                        roleUserDto = RoleUserDto.builder()
                                .userId(existingUser.getId())
                                .roleId(roleCandidate.getId())
                                .build();
                        roleUserService.createRoleUser(roleUserDto);
                    }
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setAccessMethod(AccessMethod.GOOGLE.name());
                    newUser.setIsEmailVerified(true);
                    userRepository.save(newUser);

                    RoleUserDto roleUserDto = RoleUserDto.builder()
                            .userId(newUser.getId())
                            .roleId(roleCandidate.getId())
                            .build();
                    roleUserService.createRoleUser(roleUserDto);

                    CandidateDto candidateDto = CandidateDto.builder()
                            .userId(newUser.getId())
                            .build();
                    candidateService.createCandidate(candidateDto);

                    return newUser;
                });
        return user;
    }
}
