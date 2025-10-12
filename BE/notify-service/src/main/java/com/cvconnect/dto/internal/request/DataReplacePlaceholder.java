package com.cvconnect.dto.internal.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataReplacePlaceholder {
    // ${jobPosition}
    private Long positionId;
    private String positionName;

    // ${postTitle}
    private Long jobAdId;
    private String jobAdName;

    // ${currentRound}
    private Long jobAdProcessId;
    private String jobAdProcessName;

    // ${interviewLink}
    private String interviewLink;

    // ${orgName}
    private Long orgId;
    private String orgName;

    // ${orgAddress}
    private Long orgAddressId;
    private String orgAddress;

    // ${candidateName}
    private Long candidateInfoApplyId;
    private String candidateName;

    // ${hrName}, ${hrPhone}, ${hrEmail}
    private Long hrContactId;
    private String hrName;
    private String hrPhone;
    private String hrEmail;

    //${examDate}, ${startTime}, ${endTime}
    private Instant examStartTime;
    private Instant examEndTime;

    // ${examDuration}
    private Integer examDuration;

    // ${interview-examLocation}
    private Long locationId;
    private String locationName;

}
