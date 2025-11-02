package com.cvconnect.dto.jobAd;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdUpdateRequest {
    private Long id;
    @NotBlank(message = Messages.JOB_AD_TITLE_REQUIRED)
    private String title;
    @NotNull(message = Messages.DUE_DATE_REQUIRED)
    private Instant dueDate;
    @NotNull(message = Messages.QUANTITY_REQUIRED)
    @Min(value = 1, message = Messages.QUANTITY_MIN_LENGTH)
    private Integer quantity;

    private String keyword;
    @NotBlank(message = Messages.DESCRIPTION_REQUIRED)
    private String description;
    private String requirement;
    private String benefit;

    private Boolean isAutoSendEmail;
    private Long emailTemplateId;
}
