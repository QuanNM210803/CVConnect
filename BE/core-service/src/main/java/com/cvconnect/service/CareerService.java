package com.cvconnect.service;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import nmquan.commonlib.dto.response.FilterResponse;

import java.util.List;
import java.util.Map;

public interface CareerService {
    void create(List<CareerDto> careerDtos);
    void deleteByIds(List<Long> deleteIds);
    FilterResponse<CareerDto> filter(CareerFilterRequest request);
}
