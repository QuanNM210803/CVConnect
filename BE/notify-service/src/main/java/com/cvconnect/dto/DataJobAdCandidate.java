package com.cvconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DataJobAdCandidate extends BaseDto<Instant> {
    private Long jobAdId;

    private Long candidateInfoId;

    private Instant applyDate;

    private String candidateStatus;

    private String eliminateReasonType;

    private String eliminateReasonDetail;

    private Instant onboardDate;

    private Instant eliminateDate;

    // attribute expansion
    private ConversationDto conversation;
    private String jobAdTitle;
    private String fullName;
}
