package com.cvconnect.dto.jobAd;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.positionProcess.PositionProcessRequest;
import com.cvconnect.enums.CurrencyType;
import com.cvconnect.enums.JobAdStatus;
import com.cvconnect.enums.JobType;
import com.cvconnect.enums.SalaryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdRequest {
    private Long id;

    @NotBlank(message = Messages.JOB_AD_TITLE_REQUIRED)
    private String title;
    @NotNull(message = Messages.POSITION_ID_REQUIRED)
    private Long positionId;
    @NotNull(message = Messages.POSITION_LEVEL_ID_REQUIRED)
    private Long positionLevelId;
    private List<Long> industrySubIds;
    @NotNull(message = Messages.WORK_LOCATION_ID_REQUIRED)
    private List<Long> workLocationIds;
    @NotNull(message = Messages.JOB_TYPE_REQUIRED)
    private JobType jobType;
    @NotNull(message = Messages.DUE_DATE_REQUIRED)
    private Instant dueDate;
    @NotNull(message = Messages.QUANTITY_REQUIRED)
    @Min(value = 1, message = Messages.QUANTITY_MIN_LENGTH)
    private Integer quantity;

    @NotNull(message = Messages.SALARY_TYPE_REQUIRED)
    private SalaryType salaryType;
    private Integer salaryFrom;
    private Integer salaryTo;
    @NotNull(message = Messages.CURRENCY_TYPE_REQUIRED)
    private CurrencyType currencyType;
    private String keyword;
    @NotBlank(message = Messages.DESCRIPTION_REQUIRED)
    private String description;
    private String requirement;
    private String benefit;
    @NotNull(message = Messages.HR_CONTACT_ID_REQUIRED)
    private Long hrContactId;
    @NotNull(message = Messages.JOB_AD_STATUS_REQUIRED)
    private JobAdStatus jobAdStatus;

    private boolean isPublic = true;
    private boolean isAutoSendEmail = false;
    private Long emailTemplateId;
    @Valid
    @NotNull
    private List<PositionProcessRequest> positionProcess;
    private boolean isRemote = false;

    private Long orgId;

}
