package com.cvconnect.dto.candidateEvaluation;

import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateEvaluationDetail {
    private JobAdProcessDto jobAdProcess;
    List<CandidateEvaluationDto> evaluations;
}
