package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.enums.CandidateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateOnboardFilterRequest extends FilterRequest {
    private Instant onboardDateStart;
    private Instant onboardDateEnd;
    private Instant applyDateStart;
    private Instant applyDateEnd;
    private String fullName;
    private String email;
    private String phone;
    private String jobAdTitle;
    private Long hrContactId;
    private Long levelId;
    private CandidateStatus status;

    // add
    private Long orgId;
}
