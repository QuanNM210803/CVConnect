package com.cvconnect.dto.common;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AssignRoleRequest {
    @NotNull(message = Messages.USER_NOT_FOUND)
    private Long userId;
    @NotNull(message = Messages.ROLE_NOT_FOUND)
    private List<Long> roleIds;
}
