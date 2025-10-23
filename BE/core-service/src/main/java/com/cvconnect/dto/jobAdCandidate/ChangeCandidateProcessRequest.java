package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.constant.Messages;
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
public class ChangeCandidateProcessRequest {
    @NotNull(message = Messages.PROCESS_TYPE_REQUIRE)
    private Long toJobAdProcessCandidateId;
    private boolean isSendEmail;

    private Long emailTemplateId;

    private String subject;
    private String template;
    private List<Long> placeholderIds;
}
