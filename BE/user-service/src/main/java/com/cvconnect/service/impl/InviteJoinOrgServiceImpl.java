package com.cvconnect.service.impl;

import com.cvconnect.dto.inviteJoinOrg.InviteJoinOrgDto;
import com.cvconnect.entity.InviteJoinOrg;
import com.cvconnect.repository.InviteJoinOrgRepository;
import com.cvconnect.service.InviteJoinOrgService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class InviteJoinOrgServiceImpl implements InviteJoinOrgService {
    @Autowired
    private InviteJoinOrgRepository inviteJoinOrgRepository;

    @Override
    public void create(List<InviteJoinOrgDto> dtos) {
        List<InviteJoinOrg> entities = ObjectMapperUtils.convertToList(dtos, InviteJoinOrg.class);
        inviteJoinOrgRepository.saveAll(entities);
    }

    @Override
    public InviteJoinOrgDto findByToken(String token) {
        InviteJoinOrg entity = inviteJoinOrgRepository.findByToken(token);
        if(ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(entity, InviteJoinOrgDto.class);
    }
}
