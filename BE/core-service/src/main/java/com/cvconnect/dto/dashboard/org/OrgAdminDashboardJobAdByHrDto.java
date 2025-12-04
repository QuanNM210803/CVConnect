package com.cvconnect.dto.dashboard.org;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgAdminDashboardJobAdByHrDto {
    private String hrName;
    private Long jobAdCount;
    private Long totalApplications;
    private Long totalOnboarded;
}
