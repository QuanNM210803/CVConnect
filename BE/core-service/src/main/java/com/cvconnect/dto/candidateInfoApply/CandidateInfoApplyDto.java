package com.cvconnect.dto.candidateInfoApply;

import com.cvconnect.dto.attachFile.AttachFileDto;
import com.cvconnect.dto.candidateSummaryOrg.CandidateSummaryOrgDto;
import com.cvconnect.dto.enums.CandidateStatusDto;
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
public class CandidateInfoApplyDto extends BaseDto<Instant> {
    private Long candidateId;

    private String fullName;

    private String email;

    private String phone;

    private Long cvFileId;

    private String coverLetter;

    // attribute expansion
    private AttachFileDto attachFile;

    private CandidateSummaryOrgDto candidateSummaryOrg;

    private String avatarUrl;

    private Boolean hasSchedule;

    private CandidateStatusDto candidateStatus;
    private Instant applyDate;
    private Instant onboardDate;

    private String cvUrl;
}
