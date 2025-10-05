package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.OrgIndustryDto;
import com.cvconnect.dto.org.OrganizationRequest;
import com.cvconnect.entity.Organization;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.OrgRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private OrgAddressService orgAddressService;
    @Autowired
    private OrgIndustryService orgIndustryService;
    @Autowired
    private IndustryService industryService;
    @Autowired
    private AttachFileService attachFileService;

    @Override
    @Transactional
    public IDResponse<Long> createOrg(OrganizationRequest request, MultipartFile[] files) {
        List<Long> attachFileIds = attachFileService.uploadFile(files);

        Organization org = new Organization();
        org.setName(request.getName());
        org.setDescription(request.getDescription());
        org.setWebsite(request.getWebsite());
        org.setStaffCountFrom(request.getStaffCountFrom());
        org.setStaffCountTo(request.getStaffCountTo());
        org.setLogoId(attachFileIds.get(0));
        org.setCoverPhotoId(attachFileIds.size() > 1 ? attachFileIds.get(1) : null);
        orgRepository.save(org);

        if(request.getIndustryIds() != null && !request.getIndustryIds().isEmpty()) {
            if(request.getIndustryIds().size() > Constants.MAX_INDUSTRY_PER_ORG) {
                throw new AppException(CoreErrorCode.INDUSTRY_EXCEED_LIMIT, Constants.MAX_INDUSTRY_PER_ORG);
            }
            List<IndustryDto> industryDtos = industryService.findByIds(request.getIndustryIds()).stream()
                    .filter(BaseDto::getIsActive)
                    .toList();
            if(request.getIndustryIds().size() != industryDtos.size()) {
                throw new AppException(CoreErrorCode.INDUSTRY_NOT_FOUND);
            }
            List<OrgIndustryDto> industries = request.getIndustryIds().stream()
                            .map(id -> OrgIndustryDto.builder()
                                    .orgId(org.getId())
                                    .industryId(id)
                                    .build()
                            ).collect(Collectors.toList());
            orgIndustryService.createIndustries(industries);
        }

        if(request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            List<OrgAddressDto> addresses = request.getAddresses().stream()
                            .map(address -> OrgAddressDto.builder()
                                    .orgId(org.getId())
                                    .isHeadquarter(address.isHeadquarter())
                                    .province(address.getProvince())
                                    .district(address.getDistrict())
                                    .ward(address.getWard())
                                    .detailAddress(address.getDetailAddress())
                                    .build()
                            ).collect(Collectors.toList());
            if(request.isHasRemote()){
                addresses.add(OrgAddressDto.builder()
                        .orgId(org.getId())
                        .isHeadquarter(false)
                        .province(Constants.REMOTE)
                        .detailAddress(Constants.REMOTE)
                        .build());
            }
            orgAddressService.createAddresses(addresses);
        }

        return IDResponse.<Long>builder()
                .id(org.getId())
                .build();
    }

    @Override
    public OrgDto findById(Long orgId) {
        Organization org = orgRepository.findById(orgId).orElse(null);
        if(ObjectUtils.isEmpty(org)) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(org, OrgDto.class);
    }
}
