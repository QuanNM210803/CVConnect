package com.cvconnect.dto.roleUser;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UserDto;
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
public class RoleUserDto extends BaseDto<Instant> {
    private Long userId;
    private Long roleId;
    private Boolean isDefault;

    // attribute expansion
    private RoleDto role;
    private UserDto user;
}
