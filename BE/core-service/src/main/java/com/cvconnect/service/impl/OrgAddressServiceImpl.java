package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgAddressRequest;
import com.cvconnect.entity.OrganizationAddress;
import com.cvconnect.repository.OrgAddressRepository;
import com.cvconnect.service.OrgAddressService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class OrgAddressServiceImpl implements OrgAddressService {
    @Autowired
    private OrgAddressRepository orgAddressRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

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
        Long orgId = restTemplateClient.validOrgMember();
        List<OrganizationAddress> entities = orgAddressRepository.findByOrgId(orgId);
        if(entities.isEmpty()){
            return List.of();
        }
        List<OrgAddressDto> dtos = ObjectMapperUtils.convertToList(entities, OrgAddressDto.class);
        for (OrgAddressDto dto : dtos) {
            dto.setDisplayAddress(this.buildDisplayAddress(dto));
        }
        return dtos;
    }

    @Override
    public OrgAddressDto getById(Long id) {
        OrganizationAddress entity = orgAddressRepository.findById(id).orElse(null);
        if(entity == null || entity.getIsDeleted()){
            return null;
        }
        OrgAddressDto dto = ObjectMapperUtils.convertToObject(entity, OrgAddressDto.class);
        dto.setDisplayAddress(this.buildDisplayAddress(dto));
        return dto;
    }

    @Override
    @Transactional
    public void save(List<OrgAddressRequest> requests) {
        Long orgId = restTemplateClient.validOrgMember();
        // get address of org
        List<OrgAddressDto> existingAddresses = this.getAll();
        List<Long> dbIds = existingAddresses.stream()
                .map(OrgAddressDto::getId)
                .toList();
        // delete address not in request
        if(ObjectUtils.isEmpty(requests)){
            if(!dbIds.isEmpty()){
                orgAddressRepository.deleteByIds(dbIds);
            }
            return;
        }
        // get id in req
        List<Long> reqIds = requests.stream()
                .map(OrgAddressRequest::getId)
                .filter(Objects::nonNull)
                .toList();

        // check access: case reqIds contain id org other
        if(!new HashSet<>(dbIds).containsAll(reqIds)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        
        // delete address not in reqIds
        List<Long> toDeleteIds = dbIds.stream()
                .filter(id -> !reqIds.contains(id))
                .toList();
        if(!toDeleteIds.isEmpty()){
            orgAddressRepository.deleteByIds(toDeleteIds);
        }

        // update and create
        List<OrganizationAddress> addresses = requests.stream()
                .map(address -> {
                    OrganizationAddress orgAddress = new OrganizationAddress();
                    orgAddress.setId(address.getId());
                    orgAddress.setOrgId(orgId);
                    orgAddress.setIsHeadquarter(address.isHeadquarter());
                    orgAddress.setProvince(address.getProvince());
                    orgAddress.setDistrict(address.getDistrict());
                    orgAddress.setWard(address.getWard());
                    orgAddress.setDetailAddress(address.getDetailAddress());
                    return orgAddress;
                }).collect(Collectors.toList());
        orgAddressRepository.saveAll(addresses);
    }

    @Override
    public List<OrgAddressDto> getByJobAdId(Long jobAdId) {
        List<OrganizationAddress> entities = orgAddressRepository.findByJobAdId(jobAdId);
        if(entities.isEmpty()){
            return List.of();
        }
        List<OrgAddressDto> dtos = ObjectMapperUtils.convertToList(entities, OrgAddressDto.class);
        for (OrgAddressDto dto : dtos) {
            dto.setDisplayAddress(this.buildDisplayAddress(dto));
        }
        return dtos;
    }

    private String buildDisplayAddress(OrgAddressDto dto) {
        StringJoiner addressJoiner = new StringJoiner(", ");
        if (dto.getDetailAddress() != null && !dto.getDetailAddress().isEmpty()) {
            addressJoiner.add(dto.getDetailAddress());
        }
        if (dto.getWard() != null && !dto.getWard().isEmpty()) {
            addressJoiner.add(dto.getWard());
        }
        if (dto.getDistrict() != null && !dto.getDistrict().isEmpty()) {
            addressJoiner.add(dto.getDistrict());
        }
        if (dto.getProvince() != null && !dto.getProvince().isEmpty()) {
            addressJoiner.add(dto.getProvince());
        }

        return addressJoiner.toString();
    }
}
