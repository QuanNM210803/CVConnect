package com.cvconnect.service;

import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.OrganizationRequest;
import nmquan.commonlib.dto.response.IDResponse;
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

}
