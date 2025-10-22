package com.cvconnect.service;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import com.cvconnect.dto.career.CareerRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;
import java.util.Map;

public interface CareerService {
    void deleteByIds(List<Long> deleteIds);
    FilterResponse<CareerDto> filter(CareerFilterRequest request);
    CareerDto getCareerDetail(Long careerId);
    IDResponse<Long> create(CareerRequest request);
    IDResponse<Long> update(CareerRequest request);
}
