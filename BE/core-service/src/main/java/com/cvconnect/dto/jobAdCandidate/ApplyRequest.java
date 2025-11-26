package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.constant.Messages;
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
public class ApplyRequest {
    @NotNull(message = Messages.JOB_AD_REQUIRED)
    private Long jobAdId;
    private Long candidateInfoApplyId;

    private Long candidateId;
    private String fullName;
    private String email;
    private String phone;
    private String coverLetter;
}
