package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.entity.OrganizationAddress;
import com.cvconnect.repository.OrgAddressRepository;
import com.cvconnect.service.OrgAddressService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

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

    @Override
    public List<OrgAddressDto> getByOrgIdAndIds(Long orgId, List<Long> ids) {
        List<OrganizationAddress> entities = orgAddressRepository.findByOrgIdAndIdIn(orgId, ids);
        if(entities.isEmpty()){
            return List.of();
        }
        return ObjectMapperUtils.convertToList(entities, OrgAddressDto.class);
    }

    @Override
    public List<OrgAddressDto> getAll() {
        Long orgId = WebUtils.checkCurrentOrgId();
        List<OrganizationAddress> entities = orgAddressRepository.findByOrgId(orgId);
        if(entities.isEmpty()){
            return List.of();
        }
        List<OrgAddressDto> dtos = ObjectMapperUtils.convertToList(entities, OrgAddressDto.class);
        for (OrgAddressDto dto : dtos) {
            if (Constants.REMOTE.equals(dto.getDetailAddress())) {
                dto.setDisplayAddress(Constants.REMOTE);
                continue;
            }

            StringJoiner addressJoiner = new StringJoiner(", ");
            if (dto.getDetailAddress() != null) addressJoiner.add(dto.getDetailAddress());
            if (dto.getWard() != null) addressJoiner.add(dto.getWard());
            if (dto.getDistrict() != null) addressJoiner.add(dto.getDistrict());
            if (dto.getProvince() != null) addressJoiner.add(dto.getProvince());

            dto.setDisplayAddress(addressJoiner.toString());
        }
        return dtos;
    }
}
