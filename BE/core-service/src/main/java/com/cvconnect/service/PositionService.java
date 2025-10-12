package com.cvconnect.service;

import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.position.PositionFilterRequest;
import com.cvconnect.dto.position.PositionRequest;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface PositionService {
    IDResponse<Long> create(PositionRequest request);
    void changeStatusActive(ChangeStatusActiveRequest request);
    void deleteByIds(List<Long> ids);
    PositionDto detail(Long id);
    FilterResponse<PositionDto> filter(PositionFilterRequest request);
    IDResponse<Long> update(PositionRequest request);
    PositionDto findById(Long id);
}
