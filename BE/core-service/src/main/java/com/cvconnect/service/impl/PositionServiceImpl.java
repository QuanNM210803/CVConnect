package com.cvconnect.service.impl;

import com.cvconnect.dto.PositionLevelDto;
import com.cvconnect.dto.PositionLevelRequest;
import com.cvconnect.dto.PositionProcessDto;
import com.cvconnect.dto.PositionProcessRequest;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.position.PositionRequest;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.Position;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.enums.ProcessTypeEnum;
import com.cvconnect.repository.PositionRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PositionLevelService positionLevelService;
    @Autowired
    private PositionProcessService positionProcessService;
    @Autowired
    private ProcessTypeService processTypeService;

    @Override
    @Transactional
    public IDResponse<Long> create(PositionRequest request) {
        Long orgId = WebUtils.getCurrentOrgId();
        if(Objects.isNull(orgId)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        DepartmentDto departmentDto = departmentService.detail(request.getDepartmentId());
        if(Objects.isNull(departmentDto) || !Objects.equals(departmentDto.getOrgId(), orgId)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        boolean existsByCode = positionRepository.existsByCodeAndDepartmentId(request.getCode(), request.getDepartmentId());
        if(existsByCode){
            throw new AppException(CoreErrorCode.POSITION_CODE_DUPLICATED, request.getCode());
        }
        Position position = new Position();
        position.setName(request.getName());
        position.setCode(request.getCode());
        position.setDepartmentId(request.getDepartmentId());
        positionRepository.save(position);

        List<PositionLevelRequest> positionLevelRequests = request.getPositionLevel();
        if(Objects.nonNull(positionLevelRequests)){
            List<PositionLevelDto> positionLevels = positionLevelRequests.stream()
                    .map(req -> PositionLevelDto.builder()
                                .name(req.getName())
                                .levelId(req.getLevelId())
                                .positionId(position.getId())
                                .build()
                    ).collect(Collectors.toList());
            positionLevelService.create(positionLevels);
        }

        List<PositionProcessRequest> positionProcessRequests = request.getPositionProcess();
        if(Objects.nonNull(positionProcessRequests)){
            PositionProcessRequest firstProcessRequest = positionProcessRequests.get(0);
            ProcessTypeDto firstProcessType = processTypeService.detail(firstProcessRequest.getProcessTypeId());
            if(!ProcessTypeEnum.APPLY.name().equals(firstProcessType.getCode())){
                throw new AppException(CoreErrorCode.FIRST_PROCESS_MUST_BE_APPLY);
            }
            PositionProcessRequest endProcessRequest = positionProcessRequests.get(positionProcessRequests.size() -1);
            ProcessTypeDto endProcessType = processTypeService.detail(endProcessRequest.getProcessTypeId());
            if(!ProcessTypeEnum.ONBOARD.name().equals(endProcessType.getCode())){
                throw new AppException(CoreErrorCode.LAST_PROCESS_MUST_BE_ONBOARD);
            }
            List<PositionProcessDto> positionProcessDtos = positionProcessRequests.stream()
                    .map(req -> PositionProcessDto.builder()
                            .name(req.getName())
                            .processTypeId(req.getProcessTypeId())
                            .positionId(position.getId())
                            .sortOrder(req.getSortOrder())
                            .build()
                    ).collect(Collectors.toList());
            positionProcessService.create(positionProcessDtos);
        }
        return IDResponse.<Long>builder()
                .id(position.getId())
                .build();
    }

}
