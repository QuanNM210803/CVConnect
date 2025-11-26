package com.cvconnect.dto.jobAd;

import com.cvconnect.constant.Messages;
import com.cvconnect.enums.JobAdStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdStatusRequest {
    private Long jobAdId;
    @NotNull(message = Messages.JOB_AD_STATUS_NOT_NULL)
    private JobAdStatus status;
}
