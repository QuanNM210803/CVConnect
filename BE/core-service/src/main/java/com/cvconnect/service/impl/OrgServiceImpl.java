package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.attachFile.AttachFileDto;
import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.org.OrgAddressDto;
import com.cvconnect.dto.org.OrgDto;
import com.cvconnect.dto.org.OrgIndustryDto;
import com.cvconnect.dto.org.OrganizationRequest;
import com.cvconnect.entity.Organization;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.OrgRepository;
import com.cvconnect.service.*;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
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
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    @Transactional
    public IDResponse<Long> createOrg(OrganizationRequest request, MultipartFile[] files) {
        for(MultipartFile file : files) {
            CoreServiceUtils.validateImageFileInput(file);
        }
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

    @Override
    public OrgDto getOrgInfo() {
        Long orgId = restTemplateClient.validOrgMember();
        Organization org = orgRepository.findById(orgId).orElse(null);
        if(ObjectUtils.isEmpty(org)) {
            throw new AppException(CoreErrorCode.ORG_NOT_FOUND);
        }

        OrgDto orgDto = ObjectMapperUtils.convertToObject(org, OrgDto.class);
        if(org.getLogoId() != null) {
            AttachFileDto logoInfo = attachFileService.getAttachFiles(List.of(org.getLogoId())).get(0);
            orgDto.setLogoUrl(logoInfo.getSecureUrl());
        }
        if(org.getCoverPhotoId() != null) {
            AttachFileDto coverPhotoInfo = attachFileService.getAttachFiles(List.of(org.getCoverPhotoId())).get(0);
            orgDto.setCoverPhotoUrl(coverPhotoInfo.getSecureUrl());
        }

        List<IndustryDto> industryDtos = industryService.getIndustriesByOrgId(orgId);
        orgDto.setIndustryList(industryDtos);

        return orgDto;
    }

    @Override
    @Transactional
    public IDResponse<Long> updateOrgInfo(OrganizationRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        request.setAddresses(null);

        Organization org = orgRepository.findById(orgId).orElse(null);
        if(ObjectUtils.isEmpty(org)) {
            throw new AppException(CoreErrorCode.ORG_NOT_FOUND);
        }
        org.setName(request.getName());
        org.setDescription(request.getDescription());
        org.setWebsite(request.getWebsite());
        org.setStaffCountFrom(request.getStaffCountFrom());
        org.setStaffCountTo(request.getStaffCountTo());
        orgRepository.save(org);

        List<Long> idsInReq = request.getIndustryIds();
        if (idsInReq == null || idsInReq.isEmpty()) {
            orgIndustryService.deleteByOrgId(orgId);
        } else {
            if(idsInReq.size() > Constants.MAX_INDUSTRY_PER_ORG) {
                throw new AppException(CoreErrorCode.INDUSTRY_EXCEED_LIMIT, Constants.MAX_INDUSTRY_PER_ORG);
            }

            List<IndustryDto> industryDtos = industryService.getIndustriesByOrgId(orgId);
            List<Long> idsInDb = industryDtos.stream()
                    .map(BaseDto::getId)
                    .toList();

            // delete
            List<Long> deleteIds = idsInDb.stream()
                    .filter(id -> !idsInReq.contains(id))
                    .toList();
            orgIndustryService.deleteByIndustryIdsAndOrgId(deleteIds, orgId);

            // add new
            List<Long> newIds = idsInReq.stream()
                    .filter(id -> !idsInDb.contains(id))
                    .toList();
            if(!newIds.isEmpty()) {
                List<OrgIndustryDto> industries = newIds.stream()
                        .map(id -> OrgIndustryDto.builder()
                                .orgId(orgId)
                                .industryId(id)
                                .build()
                        ).collect(Collectors.toList());
                orgIndustryService.createIndustries(industries);
            }
        }

        return IDResponse.<Long>builder()
                .id(orgId)
                .build();
    }

    @Override
    @Transactional
    public IDResponse<Long> updateOrgLogo(MultipartFile file) {
        CoreServiceUtils.validateImageFileInput(file);
        Long orgId = restTemplateClient.validOrgMember();
        Organization org = orgRepository.findById(orgId).orElse(null);
        if(ObjectUtils.isEmpty(org)) {
            throw new AppException(CoreErrorCode.ORG_NOT_FOUND);
        }
        Long oldLogoId = org.getLogoId();
        Long fileId = attachFileService.uploadFile(new MultipartFile[]{file}).get(0);
        org.setLogoId(fileId);
        orgRepository.save(org);
        if(oldLogoId != null) {
            attachFileService.deleteByIds(List.of(oldLogoId));
        }
        return IDResponse.<Long>builder()
                .id(orgId)
                .build();
    }

    @Override
    @Transactional
    public IDResponse<Long> updateOrgCoverPhoto(MultipartFile file) {
        CoreServiceUtils.validateImageFileInput(file);
        Long orgId = restTemplateClient.validOrgMember();
        Organization org = orgRepository.findById(orgId).orElse(null);
        if(ObjectUtils.isEmpty(org)) {
            throw new AppException(CoreErrorCode.ORG_NOT_FOUND);
        }
        Long oldCoverPhotoId = org.getCoverPhotoId();
        Long fileId = attachFileService.uploadFile(new MultipartFile[]{file}).get(0);
        org.setCoverPhotoId(fileId);
        orgRepository.save(org);
        if(oldCoverPhotoId != null) {
            attachFileService.deleteByIds(List.of(oldCoverPhotoId));
        }
        return IDResponse.<Long>builder()
                .id(orgId)
                .build();
    }
}
