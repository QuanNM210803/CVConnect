package com.cvconnect.dto.jobAdCandidate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdProcessCandidateDto extends BaseDto<Instant> {
    private Long jobAdProcessId;

    private Long jobAdCandidateId;

    private Instant actionDate;

    private Boolean isCurrentProcess;

    // attribute expansion
    private String processName;

    private Integer sortOrder;

    public JobAdProcessCandidateDto(Long jobAdProcessId, Long jobAdCandidateId, Instant actionDate, Boolean isCurrentProcess, String processName) {
        this.jobAdProcessId = jobAdProcessId;
        this.jobAdCandidateId = jobAdCandidateId;
        this.actionDate = actionDate;
        this.isCurrentProcess = isCurrentProcess;
        this.processName = processName;
    }
}
