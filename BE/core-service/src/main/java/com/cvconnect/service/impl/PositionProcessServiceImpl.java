package com.cvconnect.service.impl;

import com.cvconnect.dto.PositionProcessDto;
import com.cvconnect.entity.PositionProcess;
import com.cvconnect.repository.PositionProcessRepository;
import com.cvconnect.service.PositionProcessService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionProcessServiceImpl implements PositionProcessService {
    @Autowired
    private PositionProcessRepository positionProcessRepository;

    @Override
    public void create(List<PositionProcessDto> dtos) {
        List<PositionProcess> positionProcesses = ObjectMapperUtils.convertToList(dtos, PositionProcess.class);
        positionProcessRepository.saveAll(positionProcesses);
    }
}
