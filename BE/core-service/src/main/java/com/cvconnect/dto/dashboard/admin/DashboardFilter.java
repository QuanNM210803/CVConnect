package com.cvconnect.dto.dashboard.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;

@Data
public class DashboardFilter extends FilterRequest {
    @NotNull
    private Instant startTime;
    private Instant endTime = Instant.now();

    // job-ad-featured
    private Long orgId;
    private String orgName;
}
