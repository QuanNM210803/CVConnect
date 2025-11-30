package com.cvconnect.dto.dashboard.admin;

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
public class DashboardOverviewDto {
    private Long numberOfApplications;
    private Long numberOfNewCandidates;
    private Long numberOfNewOrgs;
    private Long numberOfOnboard;
    private Long numberOfJobAds;
}
