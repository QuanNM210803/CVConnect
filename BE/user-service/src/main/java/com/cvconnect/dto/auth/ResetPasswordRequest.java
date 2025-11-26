package com.cvconnect.dto.auth;

import com.cvconnect.constant.Constants;
import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordRequest {
    @NotBlank(message = Messages.TOKEN_REQUIRE)
    private String token;

    @NotBlank(message = Messages.REGISTER_PASSWORD_REQUIRE)
    @Pattern(
            regexp = Constants.Pattern.PASSWORD,
            message = Messages.REGISTER_PASSWORD_INVALID
    )
    private String newPassword;
}
