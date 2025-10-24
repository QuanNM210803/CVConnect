package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.constant.MessageConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarkOnboardRequest {
    @NotNull(message = MessageConstants.DATA_NOTFOUND)
    private Long jobAdCandidateId;
    @NotNull(message = Messages.ONBOARD_STATUS_REQUIRED)
    private Boolean isOnboarded;
}
