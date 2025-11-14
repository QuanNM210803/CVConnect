package com.cvconnect.service;

import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgAddressRequest;

import java.util.List;
import java.util.Map;

public interface OrgAddressService {
    void createAddresses(List<OrgAddressDto> addresses);
    List<OrgAddressDto> getByOrgIdAndIds(Long orgId, List<Long> ids);
    List<OrgAddressDto> getAll();
    OrgAddressDto getById(Long id);
    void save(List<OrgAddressRequest> requests);
    List<OrgAddressDto> getByJobAdId(Long jobAdId);
    Map<Long, List<OrgAddressDto>> getOrgAddressByJobAdIds(List<Long> jobAdIds);
    List<OrgAddressDto> getByOrgId(Long orgId);

}
