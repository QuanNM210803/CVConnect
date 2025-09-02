package com.cvconnect.dto.roleMenu;

import com.cvconnect.enums.PermissionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleMenuDto {
    private Long id;
    private Long roleId;
    private Long menuId;
    private List<PermissionType> permissions;
}
