package com.cvconnect.service.impl;

import com.cvconnect.dto.PositionLevelDto;
import com.cvconnect.dto.PositionLevelProjection;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.entity.PositionLevel;
import com.cvconnect.repository.PositionLevelRepository;
import com.cvconnect.service.PositionLevelService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PositionLevelServiceImpl implements PositionLevelService {
    @Autowired
    private PositionLevelRepository positionLevelRepository;

    @Override
    public void create(List<PositionLevelDto> dtos) {
        List<PositionLevel> positionLevels = ObjectMapperUtils.convertToList(dtos, PositionLevel.class);
        positionLevelRepository.saveAll(positionLevels);
    }

    @Override
    public List<PositionLevelDto> getByPositionId(Long positionId) {
        List<PositionLevelProjection> positionLevels = positionLevelRepository.findByPositionId(positionId);
        if(Objects.isNull(positionLevels)){
            return List.of();
        }
        return positionLevels.stream()
                .map(projection -> PositionLevelDto.builder()
                            .id(projection.getId())
                            .name(projection.getName())
                            .levelId(projection.getLevelId())
                            .positionId(projection.getPositionId())
                            .level(LevelDto.builder()
                                    .id(projection.getLevelId())
                                    .name(projection.getLevelName())
                                    .code(projection.getLevelCode())
                                    .build())
                            .build()
                ).collect(Collectors.toList());
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        positionLevelRepository.deleteAllById(ids);
    }

    @Override
    public Map<Long, List<PositionLevelDto>> getByPositionIds(List<Long> positionIds) {
        List<PositionLevelProjection> positionLevels = positionLevelRepository.findByPositionIds(positionIds);
        if(Objects.isNull(positionLevels)){
            return Map.of();
        }
        return positionLevels.stream()
                .map(projection -> PositionLevelDto.builder()
                        .id(projection.getId())
                        .name(projection.getName())
                        .levelId(projection.getLevelId())
                        .positionId(projection.getPositionId())
                        .level(LevelDto.builder()
                                .id(projection.getLevelId())
                                .name(projection.getLevelName())
                                .code(projection.getLevelCode())
                                .build())
                        .build()
                ).collect(Collectors.groupingBy(PositionLevelDto::getPositionId));
    }
}
