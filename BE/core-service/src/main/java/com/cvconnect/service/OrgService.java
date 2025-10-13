package com.cvconnect.service;

import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.OrganizationRequest;
import nmquan.commonlib.dto.response.IDResponse;
import org.springframework.web.multipart.MultipartFile;

public interface OrgService {
    IDResponse<Long> createOrg(OrganizationRequest request, MultipartFile[] files);
    OrgDto findById(Long orgId);
    OrgDto getOrgInfo();
    IDResponse<Long> updateOrgInfo(OrganizationRequest request);
    IDResponse<Long> updateOrgLogo(MultipartFile file);
    IDResponse<Long> updateOrgCoverPhoto(MultipartFile file);

}
