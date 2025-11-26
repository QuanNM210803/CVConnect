package com.cvconnect.dto.role;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.roleMenu.RoleMenuDto;
import com.cvconnect.enums.MemberType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RoleRequest {
    private Long id;

    @NotBlank(message = Messages.ROLE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = Messages.ROLE_NAME_NOT_BLANK)
    private String name;
    @NotNull(message = Messages.ROLE_MEMBER_TYPE_NOT_BLANK)
    private MemberType memberType;

    List<RoleMenuDto> roleMenus;
}
