package com.cvconnect.service;

import com.cvconnect.dto.positionProcess.PositionProcessDto;

import java.util.List;

public interface PositionProcessService {
    void create(List<PositionProcessDto> dtos);
    List<PositionProcessDto> getByPositionId(Long positionId);
    void deleteByIds(List<Long> ids);
}
