package com.cvconnect.service.impl;

import com.cvconnect.dto.managementMember.ManagementMemberDto;
import com.cvconnect.entity.ManagementMember;
import com.cvconnect.repository.ManagementMemberRepository;
import com.cvconnect.service.ManagementMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagementMemberServiceImpl implements ManagementMemberService {
    @Autowired
    private ManagementMemberRepository managementMemberRepository;

    @Override
    public ManagementMemberDto getManagementMember(Long userId) {
        Optional<ManagementMember> managementMember = managementMemberRepository.findByUserId(userId);
        ManagementMember entity = managementMember.orElse(null);
        if(entity == null) {
            return null;
        }
        return ManagementMemberDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .build();
    }
}
