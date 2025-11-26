package com.cvconnect.service;

import com.cvconnect.dto.org.OrgIndustryDto;

import java.util.List;

public interface OrgIndustryService {
    void createIndustries(List<OrgIndustryDto> dtos);
    void deleteByOrgId(Long orgId);
    void deleteByIndustryIdsAndOrgId(List<Long> industryIds, Long orgId);
}
