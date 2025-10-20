package com.cvconnect.dto.jobAdCandidate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateFilterRequest extends FilterRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private Long numOfApplyStart;
    private Long numOfApplyEnd;
    private String jobAdTitle;
    private List<String> candidateStatuses;
    private List<Long> processTypes;
    private Instant applyDateStart;
    private Instant applyDateEnd;
    private Long hrContactId;
}
