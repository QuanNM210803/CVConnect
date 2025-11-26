package com.cvconnect.dto.auth;

import com.cvconnect.constant.Constants;
import com.cvconnect.constant.Messages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCandidateRequest {
    @NotBlank(message = Messages.REGISTER_USERNAME_REQUIRE)
    @Pattern(
            regexp = Constants.Pattern.USERNAME,
            message = Messages.REGISTER_USERNAME_INVALID
    )
    private String username;

    @NotBlank(message = Messages.REGISTER_PASSWORD_REQUIRE)
    @Pattern(
            regexp = Constants.Pattern.PASSWORD,
            message = Messages.REGISTER_PASSWORD_INVALID
    )
    private String password;

    @NotBlank(message = Messages.REGISTER_FULL_NAME_REQUIRE)
    private String fullName;

    @NotBlank(message = Messages.REGISTER_EMAIL_REQUIRE)
    @Email(message = Messages.REGISTER_EMAIL_INVALID)
    private String email;
}
