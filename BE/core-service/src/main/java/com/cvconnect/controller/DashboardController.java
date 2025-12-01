package com.cvconnect.controller;

import com.cvconnect.dto.dashboard.admin.*;
import com.cvconnect.dto.jobAd.JobAdDto;
import com.cvconnect.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/system-admin/overview")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get dashboard overview for system admin")
    public ResponseEntity<Response<DashboardOverviewDto>> getSystemAdminDashboardOverview(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getSystemAdminDashboardOverview(filter));
    }

    @GetMapping("/system-admin/percent-passed")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get percent passed for system admin")
    public ResponseEntity<Response<List<DashboardPercentPassedDto>>> getPercentPassed(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getPercentPassed(filter));
    }

    @GetMapping("/system-admin/candidate-apply-most")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get candidate apply most for system admin")
    public ResponseEntity<Response<List<DashboardApplyMostDto>>> getCandidateApplyMost(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getCandidateApplyMost(filter));
    }

    @GetMapping("/system-admin/eliminated-reason")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Percent eliminated reason for system admin")
    public ResponseEntity<Response<List<DashboardEliminatedReasonDto>>> getPercentEliminatedReason(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getPercentEliminatedReason(filter));
    }

    @GetMapping("/system-admin/job-ad-by-time")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get job ad by time for system admin")
    public ResponseEntity<Response<List<DashboardJobAdByTimeDto>>> getJobAdByTime(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getJobAdByTime(filter));
    }

    @GetMapping("/system-admin/job-ad-by-career")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get job ad by career for system admin")
    public ResponseEntity<Response<List<DashboardJobAdByCareerDto>>> getJobAdByCareer(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getJobAdByCareer(filter));
    }

    @GetMapping("/system-admin/job-ad-by-level")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get job ad by level for system admin")
    public ResponseEntity<Response<List<DashboardJobAdByLevelDto>>> getJobAdByLevel(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getJobAdByLevel(filter));
    }

    @GetMapping("/system-admin/job-ad-featured")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get job ad featured for system admin")
    public ResponseEntity<Response<List<JobAdDto>>> getJobAdFeatured(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getJobAdFeatured(filter));
    }

    @GetMapping("/system-admin/new-org-by-time")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get new organization by time for system admin")
    public ResponseEntity<Response<List<DashboardNewOrgByTimeDto>>> getNewOrgByTime(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getNewOrgByTime(filter));
    }

    @GetMapping("/system-admin/staff-size")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get organization staff size for system admin")
    public ResponseEntity<Response<List<DashboardOrgStaffSizeDto>>> getOrgStaffSize() {
        return ResponseUtils.success(dashboardService.getOrgStaffSize());
    }

    @GetMapping("/system-admin/organization-featured")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN')")
    @Operation(summary = "Get organization featured for system admin")
    public ResponseEntity<Response<FilterResponse<DashboardOrgFeaturedDto>>> getOrgFeatured(@Valid @ModelAttribute DashboardFilter filter) {
        return ResponseUtils.success(dashboardService.getOrgFeatured(filter));
    }
}
