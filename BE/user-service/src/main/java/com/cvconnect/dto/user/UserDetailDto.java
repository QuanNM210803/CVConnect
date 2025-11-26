package com.cvconnect.dto.user;

import com.cvconnect.dto.role.RoleDto;
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
public class UserDetailDto<T> {
    private RoleDto role;
    private T detailInfo;
}
