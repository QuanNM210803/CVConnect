package com.cvconnect.service;

import com.cvconnect.dto.positionLevel.PositionLevelDto;

import java.util.List;
import java.util.Map;

public interface PositionLevelService {
    void create(List<PositionLevelDto> dtos);
    List<PositionLevelDto> getByPositionId(Long positionId);
    void deleteByIds(List<Long> ids);
    Map<Long, List<PositionLevelDto>> getByPositionIds(List<Long> positionIds);
}
