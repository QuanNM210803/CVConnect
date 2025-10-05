package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteUserRequest {
    @NotNull(message = Messages.USER_NOT_FOUND)
    private Long userId;
    @NotNull(message = Messages.ROLE_NOT_FOUND)
    private Long roleId;
}
