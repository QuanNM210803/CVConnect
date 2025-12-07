package com.cvconnect.dto.user;

import com.cvconnect.constant.Constants;
import com.cvconnect.constant.Messages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.constant.CommonConstants;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {
    @NotBlank(message = Messages.REGISTER_EMAIL_REQUIRE)
    @Email(message = Messages.REGISTER_EMAIL_INVALID)
    private String email;

    @NotBlank(message = Messages.REGISTER_FULL_NAME_REQUIRE)
    private String fullName;

    @Pattern(regexp = Constants.Pattern.PHONE_NUMBER, message = Messages.REGISTER_PHONE_INVALID)
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
}
