package com.cvconnect.dto.user;

import com.cvconnect.enums.AccessMethod;
import com.cvconnect.enums.MemberType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterRequest extends FilterRequest {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirthStart;
    private LocalDate dateOfBirthEnd;
    private AccessMethod accessMethod;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private List<Long> roleIds;

    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant updatedAtStart;
    private Instant updatedAtEnd;
    private String createdBy;
    private String updatedBy;

    private List<MemberType> memberTypes;

}
