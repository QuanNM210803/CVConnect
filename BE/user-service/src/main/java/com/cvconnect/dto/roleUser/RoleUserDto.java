package com.cvconnect.dto.roleUser;

import com.cvconnect.dto.role.RoleDto;
import com.cvconnect.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleUserDto {
    private Long id;
    private Long userId;
    private Long roleId;
    private RoleDto role;
    private UserDto user;
}
