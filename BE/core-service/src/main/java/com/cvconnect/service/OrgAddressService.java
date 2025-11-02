package com.cvconnect.service;

import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgAddressRequest;

import java.util.List;

public interface OrgAddressService {
    void createAddresses(List<OrgAddressDto> addresses);
    List<OrgAddressDto> getByOrgIdAndIds(Long orgId, List<Long> ids);
    List<OrgAddressDto> getAll();
    OrgAddressDto getById(Long id);
    void save(List<OrgAddressRequest> requests);
    List<OrgAddressDto> getByJobAdId(Long jobAdId);
}
