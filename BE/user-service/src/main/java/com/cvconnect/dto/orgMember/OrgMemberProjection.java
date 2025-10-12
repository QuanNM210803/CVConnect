package com.cvconnect.dto.orgMember;

import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.LocalDate;

public interface OrgMemberProjection extends BaseRepositoryDto {
    Long getUserId();
    Long getOrgId();
    String getUsername();
    String getEmail();
    String getFullName();
    String getPhoneNumber();
    LocalDate getDateOfBirth();
    Boolean getIsEmailVerified();
    String getInviter();
}
