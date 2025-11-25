package com.cvconnect.service;

import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.OrgFilterRequest;
import com.cvconnect.dto.org.OrganizationRequest;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface OrgService {
    IDResponse<Long> createOrg(OrganizationRequest request, MultipartFile[] files);
    OrgDto findById(Long orgId);
    OrgDto getOrgInfo();
    IDResponse<Long> updateOrgInfo(OrganizationRequest request);
    IDResponse<Long> updateOrgLogo(MultipartFile file);
    IDResponse<Long> updateOrgCoverPhoto(MultipartFile file);
    Map<Long, OrgDto> getOrgMapByIds(List<Long> orgIds);
    OrgDto getOrgInfoOutside(Long orgId);
    List<OrgDto> getFeaturedOrgOutside();
    OrgDto getOrgByJobAd(Long jobAdId);
    FilterResponse<OrgDto> filterOrgs(OrgFilterRequest request);
    InputStreamResource exportOrg(OrgFilterRequest request);
    void changeStatusActive(ChangeStatusActiveRequest request);
    OrgDto getOrgDetails(Long orgId);
}
