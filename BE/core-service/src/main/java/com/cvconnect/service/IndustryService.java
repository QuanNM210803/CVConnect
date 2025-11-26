package com.cvconnect.service;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.dto.industry.IndustryRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;
import java.util.Map;

public interface IndustryService {
    List<IndustryDto> findByIds(List<Long> ids);
    FilterResponse<IndustryDto> filter(IndustryFilterRequest request);
    FilterResponse<IndustryDto> filterPublic(IndustryFilterRequest request);
    IndustryDto detail(Long id);
    void deleteByIds(List<Long> ids);
    IDResponse<Long> create(IndustryRequest request);
    IDResponse<Long> update(IndustryRequest request);
    List<IndustryDto> getIndustriesByOrgId(Long orgId);
    Map<Long, List<IndustryDto>> getMapIndustriesByOrgIds(List<Long> orgIds);
}
