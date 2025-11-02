package com.cvconnect.service;

import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.level.LevelFilterRequest;
import com.cvconnect.dto.level.LevelRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface LevelService {
    LevelDto getById(Long id);
    FilterResponse<LevelDto> filter(LevelFilterRequest levelFilter);
    IDResponse<Long> create(LevelRequest request);
    IDResponse<Long> update(LevelRequest request);
    void deleteByIds(List<Long> ids);
    List<LevelDto> getByIds(List<Long> ids);
    List<LevelDto> getLevelsByJobAdId(Long jobAdId);
}
