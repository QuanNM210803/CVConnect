package com.cvconnect.service;

import com.cvconnect.dto.inviteJoinOrg.InviteJoinOrgDto;

import java.util.List;

public interface InviteJoinOrgService {
    void create(List<InviteJoinOrgDto> dtos);
    InviteJoinOrgDto findByToken(String token);
}
