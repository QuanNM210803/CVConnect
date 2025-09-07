package com.cvconnect.service.impl;

import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.entity.OrganizationAddress;
import com.cvconnect.repository.OrgAddressRepository;
import com.cvconnect.service.OrgAddressService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgAddressServiceImpl implements OrgAddressService {
    @Autowired
    private OrgAddressRepository orgAddressRepository;
    @Override
    public void createAddresses(List<OrgAddressDto> addresses) {
        List<OrganizationAddress> entities = addresses.stream()
                .map(dto -> ObjectMapperUtils.convertToObject(dto, OrganizationAddress.class))
                .toList();
        orgAddressRepository.saveAll(entities);
    }
}
