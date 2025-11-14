package com.cvconnect.service;

import com.cvconnect.dto.jobAd.*;
import nmquan.commonlib.dto.request.FilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;
import java.util.logging.Filter;

public interface JobAdService {
    IDResponse<Long> create(JobAdRequest request);
    JobAdDto findById(Long id);
    JobAdDto findByJobAdProcessId(Long jobAdProcessId);
    List<JobAdProcessDto> getProcessByJobAdId(Long jobAdId);
    FilterResponse<JobAdOrgDetailResponse> filterJobAdsForOrg(JobAdOrgFilterRequest request);
    void updateJobAdStatus(JobAdStatusRequest request);
    void updatePublicStatus(JobAdPublicStatusRequest request);
    JobAdOrgDetailResponse getJobAdOrgDetail(Long jobAdId);
    IDResponse<Long> update(JobAdUpdateRequest request);
    FilterResponse<JobAdDto> getJobAdsByParticipantId(FilterRequest request);
    JobAdOutsideDataFilter outsideDataFilter();
    JobAdOutsideFilterResponse<JobAdOutsideDetailResponse> filterJobAdsForOutside(JobAdOutsideFilterRequest request);
}
