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
public class DashboardOrgFeaturedDto {
    private Long orgId;
    private String orgName;
    private String orgLogo;
    private Long numberOfJobAds;
    private Long numberOfApplications;
    private Long numberOfOnboarded;
}
