package com.cvconnect.service.impl;

import com.cvconnect.dto.PositionLevelDto;
import com.cvconnect.entity.PositionLevel;
import com.cvconnect.repository.PositionLevelRepository;
import com.cvconnect.service.PositionLevelService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionLevelServiceImpl implements PositionLevelService {
    @Autowired
    private PositionLevelRepository positionLevelRepository;

    @Override
    public void create(List<PositionLevelDto> dtos) {
        List<PositionLevel> positionLevels = ObjectMapperUtils.convertToList(dtos, PositionLevel.class);
        positionLevelRepository.saveAll(positionLevels);
    }
}
