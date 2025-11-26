package com.cvconnect.dto.user;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePasswordRequest {
    @NotBlank(message = Messages.CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;
    @NotBlank(message = Messages.NEW_PASSWORD_REQUIRED)
    private String newPassword;
}
