package com.cvconnect.dto.candidateInfoApply;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateInfoFilterByJobAdProcess extends FilterRequest {
    private Long jobAdProcessId;
    private String fullName;
    private String email;
    private String phone;
    private List<String> candidateStatuses; // khong sort
    private List<Long> levelIds;
    private Instant applyDateStart;
    private Instant applyDateEnd;
    private Instant onboardDateStart;
    private Instant onboardDateEnd;

    private Long orgId;
}
