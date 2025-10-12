package com.cvconnect.dto.orgMember;

import com.cvconnect.dto.internal.response.OrgDto;
import com.cvconnect.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.BaseRepositoryDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgMemberDto extends UserDto {
    private Long userId;
    private Long orgId;
    private String inviter;

    private OrgDto org;

    public Long id;
    public Boolean isActive;
    public Boolean isDeleted;
    public String createdBy;
    public String updatedBy;
    public Instant createdAt;
    public Instant updatedAt;
}
