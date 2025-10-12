package com.cvconnect.dto.orgMember;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgMemberFilter extends FilterRequest {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirthStart;
    private Date dateOfBirthEnd;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private List<Long> roleIds;

    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant updatedAtStart;
    private Instant updatedAtEnd;
    private String inviter;
    private String updatedBy;

    private Long orgId;

}
