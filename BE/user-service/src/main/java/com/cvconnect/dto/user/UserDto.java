package com.cvconnect.dto.user;

import com.cvconnect.dto.role.RoleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
    private String address;
    private LocalDate dateOfBirth;
    private Boolean isEmailVerified;
    private String accessMethod;

    private Boolean isActive;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private List<RoleDto> roles;

    private List<UserDetailDto> userDetails;
}
