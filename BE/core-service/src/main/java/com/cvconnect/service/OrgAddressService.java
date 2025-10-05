package com.cvconnect.service;

import com.cvconnect.dto.org.OrgAddressDto;

import java.util.List;

public interface OrgAddressService {
    void createAddresses(List<OrgAddressDto> addresses);
    List<OrgAddressDto> getByOrgIdAndIds(Long orgId, List<Long> ids);
    List<OrgAddressDto> getAll();
}
