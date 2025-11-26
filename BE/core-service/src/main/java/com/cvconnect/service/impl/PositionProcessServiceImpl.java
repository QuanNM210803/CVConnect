package com.cvconnect.service.impl;

import com.cvconnect.dto.positionProcess.PositionProcessDto;
import com.cvconnect.dto.positionProcess.PositionProcessProjection;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.PositionProcess;
import com.cvconnect.repository.PositionProcessRepository;
import com.cvconnect.service.PositionProcessService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PositionProcessServiceImpl implements PositionProcessService {
    @Autowired
    private PositionProcessRepository positionProcessRepository;

    @Override
    public void create(List<PositionProcessDto> dtos) {
        List<PositionProcess> positionProcesses = ObjectMapperUtils.convertToList(dtos, PositionProcess.class);
        positionProcessRepository.saveAll(positionProcesses);
    }

    @Override
    public List<PositionProcessDto> getByPositionId(Long positionId) {
        List<PositionProcessProjection> positionProcesses = positionProcessRepository.findByPositionId(positionId);
        if(Objects.isNull(positionProcesses)) {
            return List.of();
        }
        return positionProcesses.stream()
                .map(projection -> PositionProcessDto.builder()
                        .id(projection.getId())
                        .name(projection.getName())
                        .positionId(projection.getPositionId())
                        .processTypeId(projection.getProcessId())
                        .sortOrder(projection.getSortOrder())
                        .processType(ProcessTypeDto.builder()
                                .id(projection.getProcessId())
                                .name(projection.getProcessName())
                                .code(projection.getProcessCode())
                                .build())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if(Objects.nonNull(ids) && !ids.isEmpty()){
            positionProcessRepository.deleteAllById(ids);
        }
    }
}
