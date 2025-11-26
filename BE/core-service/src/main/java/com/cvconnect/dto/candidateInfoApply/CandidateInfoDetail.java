package com.cvconnect.dto.candidateInfoApply;

import com.cvconnect.dto.jobAdCandidate.JobAdCandidateDto;
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
public class CandidateInfoDetail {
    private CandidateInfoApplyDto candidateInfo;
    private List<JobAdCandidateDto> jobAdCandidates;
}
