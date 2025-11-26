package com.cvconnect.dto.candidateEvaluation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateEvaluationDto extends BaseDto<Instant> {
    private Long jobAdProcessCandidateId;

    private Long evaluatorId;

    private String comments;

    private BigDecimal score;

    // attributes expansion
    private String evaluatorName;
    private String evaluatorUsername;
}
