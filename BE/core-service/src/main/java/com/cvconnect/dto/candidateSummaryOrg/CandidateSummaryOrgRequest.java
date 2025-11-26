package com.cvconnect.dto.candidateSummaryOrg;

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
public class CandidateSummaryOrgRequest {
    private String skill;
    private Long levelId;
    @NotNull(message = Messages.CANDIDATE_INFO_APPLY_NOT_FOUND)
    private Long candidateInfoId;
}
