package com.cvconnect.service;

import com.cvconnect.dto.org.OrganizationRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface OrgService {
    IDResponse<Long> createOrg(OrganizationRequest request);
}
