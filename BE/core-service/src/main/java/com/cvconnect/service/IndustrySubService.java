package com.cvconnect.service;

import com.cvconnect.dto.IndustrySubDto;

import java.util.List;
import java.util.Map;

public interface IndustrySubService {
    Map<Long, List<IndustrySubDto>> getIndustryIds(List<Long> industryIds);
    void create(List<IndustrySubDto> industrySubDtos);
    void deleteByIds(List<Long> deleteIds);
}
