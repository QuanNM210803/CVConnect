package com.cvconnect.service;

import com.cvconnect.dto.position.PositionRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface PositionService {
    IDResponse<Long> create(PositionRequest request);
}
