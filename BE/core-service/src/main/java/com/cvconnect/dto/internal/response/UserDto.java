package com.cvconnect.dto.internal.response;

//import com.cvconnect.dto.role.RoleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends BaseDto<Instant> {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private Long avatarId;
    private Boolean isEmailVerified;
    private String accessMethod;

//    private List<RoleDto> roles;
//    private List<UserDetailDto> userDetails;
    private String avatarUrl;
    private Long orgId;

    public UserDto configResponse() {
        this.setIsDeleted(null);
        this.setPassword(null);
        return this;
    }
}
