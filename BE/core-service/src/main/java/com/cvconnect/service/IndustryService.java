package com.cvconnect.service;

import com.cvconnect.dto.industry.IndustryDto;

import java.util.List;

public interface IndustryService {
    List<IndustryDto> findByIds(List<Long> ids);
}
