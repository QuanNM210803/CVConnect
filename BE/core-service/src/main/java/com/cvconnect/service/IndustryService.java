package com.cvconnect.service;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;

public interface IndustryService {
    List<IndustryDto> findByIds(List<Long> ids);
    FilterResponse<IndustryDto> filter(IndustryFilterRequest request);
    FilterResponse<IndustryDto> filterPublic(IndustryFilterRequest request);
}
