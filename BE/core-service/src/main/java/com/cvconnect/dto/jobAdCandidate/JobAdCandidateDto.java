package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
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
public class JobAdCandidateDto extends BaseDto<Instant> {
    private Long jobAdId;

    private Long candidateInfoId;

    private Instant applyDate;

    private String candidateStatus;

    private String eliminateReasonType;

    private String eliminateReasonDetail;

    private Instant onboardDate;

    // attribute expansion
    private JobAdDto jobAd;
    private ProcessTypeDto currentRound;
}
