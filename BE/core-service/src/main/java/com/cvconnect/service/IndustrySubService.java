package com.cvconnect.service;

import com.cvconnect.dto.industrySub.IndustrySubDto;
import com.cvconnect.dto.industrySub.IndustrySubFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;
import java.util.Map;

public interface IndustrySubService {
    Map<Long, List<IndustrySubDto>> getIndustryIds(List<Long> industryIds);
    void create(List<IndustrySubDto> industrySubDtos);
    void deleteByIds(List<Long> deleteIds);
    FilterResponse<IndustrySubDto> filter(IndustrySubFilterRequest request);
}
