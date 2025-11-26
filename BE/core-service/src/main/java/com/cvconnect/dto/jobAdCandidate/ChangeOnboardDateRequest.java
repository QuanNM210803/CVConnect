package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.constant.MessageConstants;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeOnboardDateRequest {
    @NotNull(message = MessageConstants.DATA_NOTFOUND)
    private Long jobAdCandidateId;
    @NotNull(message = Messages.ONBOARD_DATE_REQUIRED)
    private Instant newOnboardDate;
}
