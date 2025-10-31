package com.cvconnect.dto.jobAd;

import com.cvconnect.enums.JobAdStatus;
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
public class JobAdOrgFilterRequest extends FilterRequest {
    private String keyword; // keyword search in title, position
    private JobAdStatus jobAdStatus;

    // hide
    private Boolean isPublic;
    private List<Long> departmentIds;
    private Long hrContactId;
    private String createdBy;
    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant dueDateStart;
    private Instant dueDateEnd;

    private Long orgId;

    // allow sort by: createdAt, dueDate, title


}
