package com.cvconnect.dto.dashboard.org;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;

@Data
public class OrgAdminDashboardFilter extends FilterRequest {
    @NotNull
    private Instant startTime;
    private Instant endTime = Instant.now();

    private Long orgId;
}
