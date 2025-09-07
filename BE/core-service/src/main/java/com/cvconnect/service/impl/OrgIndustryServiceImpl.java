package com.cvconnect.service.impl;

import com.cvconnect.dto.org.OrgIndustryDto;
import com.cvconnect.entity.OrganizationIndustry;
import com.cvconnect.repository.OrgIndustryRepository;
import com.cvconnect.service.OrgIndustryService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgIndustryServiceImpl implements OrgIndustryService {
    @Autowired
    private OrgIndustryRepository orgIndustryRepository;

    @Override
    public void createIndustries(List<OrgIndustryDto> dtos) {
        List<OrganizationIndustry> entities = dtos.stream()
                .map(dto -> ObjectMapperUtils.convertToObject(dto, OrganizationIndustry.class))
                .toList();
        orgIndustryRepository.saveAll(entities);
    }
}
