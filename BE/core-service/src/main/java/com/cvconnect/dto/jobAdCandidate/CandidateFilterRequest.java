package com.cvconnect.dto.jobAdCandidate;

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
public class CandidateFilterRequest extends FilterRequest {
    private String fullName;
    private String email;
    private String phone;
    private List<Long> levelIds;
    private Long numOfApplyStart;
    private Long numOfApplyEnd;
    private String jobAdTitle;
    private List<String> candidateStatuses;
    private List<Long> processTypes;
    private Instant applyDateStart;
    private Instant applyDateEnd;
    private Long hrContactId;
}
