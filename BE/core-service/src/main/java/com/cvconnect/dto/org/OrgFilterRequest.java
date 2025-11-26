package com.cvconnect.dto.org;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgFilterRequest extends FilterRequest {
    private String orgName;
    private String website;
    private Long staffCountFrom;
    private Long staffCountTo;
    private List<String> addresses;
    private List<Long> industryIds;

    private Boolean isActive;
    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant updatedAtStart;
    private Instant updatedAtEnd;
    private String createdBy;
    private String updatedBy;
}
