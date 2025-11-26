package com.cvconnect.dto.inviteJoinOrg;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteJoinOrgDto extends BaseDto<Instant> {
    private Long userId;
    private Long roleId;
    private Long orgId;
    private String status;
    private String token;
}
