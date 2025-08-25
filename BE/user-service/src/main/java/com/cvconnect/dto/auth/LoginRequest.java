package com.cvconnect.dto.auth;

import com.cvconnect.constant.Messages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = Messages.LOGIN_USERNAME_REQUIRE)
    private String username;
    @NotBlank(message = Messages.LOGIN_PASSWORD_REQUIRE)
    private String password;
}
