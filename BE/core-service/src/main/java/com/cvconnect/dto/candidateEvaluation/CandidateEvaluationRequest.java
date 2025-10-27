package com.cvconnect.dto.candidateEvaluation;

import com.cvconnect.constant.Messages;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateEvaluationRequest {
    private Long id;
    private Long jobAdCandidateId;
    @NotBlank(message = Messages.COMMENTS_NOT_BLANK)
    private String comments;
    private BigDecimal score;
}
