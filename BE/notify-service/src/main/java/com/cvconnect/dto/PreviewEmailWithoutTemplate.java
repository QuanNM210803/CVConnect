package com.cvconnect.dto;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.internal.request.DataReplacePlaceholder;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviewEmailWithoutTemplate {
    @NotBlank(message = Messages.EMAIL_TEMPLATE_SUBJECT_NOT_BLANK)
    private String subject;
    @NotBlank(message = Messages.EMAIL_TEMPLATE_BODY_NOT_BLANK)
    private String body;
    private List<String> placeholderCodes;
    private DataReplacePlaceholder dataReplacePlaceholder;
}
