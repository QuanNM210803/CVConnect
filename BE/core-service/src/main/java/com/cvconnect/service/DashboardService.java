package com.cvconnect.service;

import com.cvconnect.dto.dashboard.admin.*;
import com.cvconnect.dto.dashboard.org.*;
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

    // orgadmin
    OrgAdminDashboardOverviewDto getOrgAdminDashboardOverview(OrgAdminDashboardFilter filter);
    List<DashboardPercentPassedDto> getOrgAdminPercentPassed(OrgAdminDashboardFilter filter);
    List<OrgAdminDashboardJobAdByHrDto> getOrgAdminJobAdByHr(OrgAdminDashboardFilter filter);
    List<OrgAdminDashboardJobAdByDepartmentDto> getOrgAdminJobAdByDepartment(OrgAdminDashboardFilter filter);
    List<OrgAdminDashboardPassByLevelDto> getOrgAdminPassByLevel(OrgAdminDashboardFilter filter);
    List<DashboardEliminatedReasonDto> getOrgAdminPercentEliminatedReason(OrgAdminDashboardFilter filter);
    List<JobAdDto> getOrgAdminJobAdFeatured(OrgAdminDashboardFilter filter);
}
