package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateFilterResponse {
    private CandidateInfoApplyDto candidateInfo;
    private Long numOfApply;
    private List<JobAdCandidateDto> jobAdCandidates;
}
