package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
public class EmailConfigRequest {
    private Long id;
    @NotBlank(message = Messages.EMAIL_CONFIG_HOST_REQUIRED)
    private String host;
    @NotNull(message = Messages.EMAIL_CONFIG_PORT_REQUIRED)
    private Integer port;
    @NotBlank(message = Messages.EMAIL_CONFIG_EMAIL_REQUIRED)
    private String email;
    @NotBlank(message = Messages.EMAIL_CONFIG_PASSWORD_REQUIRED)
    private String password;
    @NotNull(message = Messages.EMAIL_CONFIG_IS_SSL_REQUIRED)
    private Boolean isSsl;
    @NotBlank(message = Messages.EMAIL_CONFIG_PROTOCOL_REQUIRED)
    private String protocol;
}
