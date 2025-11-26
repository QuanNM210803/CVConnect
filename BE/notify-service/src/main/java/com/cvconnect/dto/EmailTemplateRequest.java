package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailTemplateRequest {
    private Long id;
    @NotBlank(message = Messages.EMAIL_TEMPLATE_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = Messages.EMAIL_TEMPLATE_NAME_NOT_BLANK)
    private String name;
    @NotBlank(message = Messages.EMAIL_TEMPLATE_SUBJECT_NOT_BLANK)
    private String subject;
    @NotBlank(message = Messages.EMAIL_TEMPLATE_BODY_NOT_BLANK)
    private String body;

    private List<Long> placeholderIds;
}
