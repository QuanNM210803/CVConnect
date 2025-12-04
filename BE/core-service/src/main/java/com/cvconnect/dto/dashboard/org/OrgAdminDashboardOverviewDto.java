package com.cvconnect.dto.dashboard.org;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgAdminDashboardOverviewDto {
    private Long totalJobAds;
    private String indeTotalJobAds;
    private Long totalOpenJobAds;
    private Long totalApplications;
    private String indeTotalApplications;
    private Long totalCandidateInProcess;
    private Double percentPassed;
    private String indePercentPassed;
}
