package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.constant.Messages;
import com.cvconnect.enums.EliminateReasonEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
public class EliminateCandidateRequest {
    @NotNull(message = Messages.CANDIDATE_INFO_APPLY_NOT_FOUND)
    private Long jobAdCandidateId;
    @NotNull(message = Messages.ELIMINATE_REASON_REQUIRE)
    private EliminateReasonEnum reason;
    private String reasonDetail;
    private boolean isSendEmail;

    private Long emailTemplateId;

    private String subject;
    private String template;
    private List<String> placeholders;

}
