package com.cvconnect.dto.jobAdCandidate;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoApplyDto;
import com.cvconnect.dto.enums.CandidateStatusDto;
import com.cvconnect.dto.enums.EliminateReasonEnumDto;
import com.cvconnect.dto.internal.response.ConversationDto;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.util.List;

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

    private Instant eliminateDate;

    // attribute expansion
    private CandidateStatusDto candidateStatusDto;
    private EliminateReasonEnumDto eliminateReason;
    private JobAdDto jobAd;
    private ProcessTypeDto currentRound;
    private List<JobAdProcessCandidateDto> jobAdProcessCandidates;
    private CandidateInfoApplyDto candidateInfo;
    private OrgDto org;
    private boolean hasMessageUnread;
    private ConversationDto conversation;
}
