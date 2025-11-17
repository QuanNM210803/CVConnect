package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.enums.CandidateStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdAppliedFilterRequest extends FilterRequest {
    private CandidateStatus candidateStatus;
    private Long userId;
}
