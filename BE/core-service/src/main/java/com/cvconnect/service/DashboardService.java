package com.cvconnect.service;

import com.cvconnect.dto.dashboard.admin.*;
import com.cvconnect.dto.jobAd.JobAdDto;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

public interface DashboardService {
    DashboardOverviewDto getSystemAdminDashboardOverview(DashboardFilter filter);
    List<DashboardPercentPassedDto> getPercentPassed(DashboardFilter filter);
    List<DashboardApplyMostDto> getCandidateApplyMost(DashboardFilter filter);
    List<DashboardEliminatedReasonDto> getPercentEliminatedReason(DashboardFilter filter);
    List<DashboardJobAdByTimeDto> getJobAdByTime(DashboardFilter filter);
    List<DashboardJobAdByLevelDto> getJobAdByLevel(DashboardFilter filter);
    List<DashboardJobAdByCareerDto> getJobAdByCareer(DashboardFilter filter);
    List<JobAdDto> getJobAdFeatured(DashboardFilter filter);
    List<DashboardNewOrgByTimeDto> getNewOrgByTime(DashboardFilter filter);
    List<DashboardOrgStaffSizeDto> getOrgStaffSize();
    FilterResponse<DashboardOrgFeaturedDto> getOrgFeatured(DashboardFilter filter);
}
