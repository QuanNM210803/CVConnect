package com.cvconnect.service.impl;
import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.entity.OrgMember;
import com.cvconnect.repository.OrgMemberRepository;
import com.cvconnect.service.OrgMemberService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgMemberServiceImpl implements OrgMemberService {
    @Autowired
    private OrgMemberRepository orgMemberRepository;
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
}
