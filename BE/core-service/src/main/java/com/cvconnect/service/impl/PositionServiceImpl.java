package com.cvconnect.service.impl;

import com.cvconnect.dto.common.ChangeStatusActiveRequest;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.position.PositionFilterRequest;
import com.cvconnect.dto.position.PositionRequest;
import com.cvconnect.dto.positionLevel.PositionLevelDto;
import com.cvconnect.dto.positionLevel.PositionLevelRequest;
import com.cvconnect.dto.positionProcess.PositionProcessDto;
import com.cvconnect.dto.positionProcess.PositionProcessRequest;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.Position;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.enums.ProcessTypeEnum;
import com.cvconnect.repository.PositionRepository;
import com.cvconnect.service.*;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
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
        DepartmentDto departmentDto = departmentService.detail(request.getDepartmentId());
        if(Objects.isNull(departmentDto)){
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

        this.savePositionLevels(request.getPositionLevel(), position.getId());
        this.savePositionProcesses(request.getPositionProcess(), position.getId());
        return IDResponse.<Long>builder()
                .id(position.getId())
                .build();
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        Long orgId = WebUtils.checkCurrentOrgId();
        List<Position> positions = positionRepository.findByIdsAndOrgId(request.getIds(), orgId);
        if(positions.size() != request.getIds().size()){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        positions.forEach(position -> position.setIsActive(request.getActive()));
        positionRepository.saveAll(positions);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        Long orgId = WebUtils.checkCurrentOrgId();
        List<Position> positions = positionRepository.findByIdsAndOrgId(ids, orgId);
        if(positions.size() != ids.size()){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        positionRepository.deleteAll(positions);
    }

    @Override
    public PositionDto detail(Long id) {
        Long orgId = WebUtils.checkCurrentOrgId();
        Position position = positionRepository.findByIdAndOrgId(id, orgId);
        if(Objects.isNull(position)){
            throw new AppException(CoreErrorCode.POSITION_NOT_FOUND);
        }
        List<PositionLevelDto> listLevel = positionLevelService.getByPositionId(position.getId());
        List<PositionProcessDto> listProcess = positionProcessService.getByPositionId(position.getId());

        PositionDto positionDto = ObjectMapperUtils.convertToObject(position, PositionDto.class);
        positionDto.setListLevel(listLevel);
        positionDto.setListProcess(listProcess);
        return positionDto;
    }

    @Override
    public FilterResponse<PositionDto> filter(PositionFilterRequest request) {
        Long orgId = WebUtils.checkCurrentOrgId();
        request.setOrgId(orgId);
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<PositionDto> page = positionRepository.filter(request, request.getPageable());

        // get list position level
        List<PositionDto> positionDtos = page.getContent();
        List<Long> positionIds = positionDtos.stream()
                .map(PositionDto::getId)
                .toList();
        Map<Long, List<PositionLevelDto>> mapPositionLevel = positionLevelService.getByPositionIds(positionIds);
        positionDtos.forEach(positionDto ->
                positionDto.setListLevel(mapPositionLevel.get(positionDto.getId()))
        );
        return PageUtils.toFilterResponse(page, positionDtos);
    }

    @Override
    @Transactional
    public IDResponse<Long> update(PositionRequest request) {
        Long orgId = WebUtils.checkCurrentOrgId();
        DepartmentDto departmentDto = departmentService.detail(request.getDepartmentId());
        if(Objects.isNull(departmentDto)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        Position position = positionRepository.findByIdAndOrgId(request.getId(), orgId);
        if(Objects.isNull(position)){
            throw new AppException(CoreErrorCode.POSITION_NOT_FOUND);
        }
        boolean existsByCode = positionRepository.existsByCodeAndDepartmentId(request.getCode(), request.getDepartmentId());
        if(existsByCode && !position.getCode().equals(request.getCode())){
            throw new AppException(CoreErrorCode.POSITION_CODE_DUPLICATED, request.getCode());
        }
        position.setName(request.getName());
        position.setCode(request.getCode());
        position.setDepartmentId(request.getDepartmentId());
        positionRepository.save(position);

        // Xoá những position level không còn trong request
        List<PositionLevelRequest> plRequests = request.getPositionLevel();
        List<PositionLevelDto> plDtos = positionLevelService.getByPositionId(position.getId());
        List<Long> deleteIdsPositionLevel = WebUtils.getDeleteIds(plRequests, plDtos);
        positionLevelService.deleteByIds(deleteIdsPositionLevel);

        // Xoá những position process không còn trong request
        List<PositionProcessRequest> ppRequests = request.getPositionProcess();
        List<PositionProcessDto> ppDtos = positionProcessService.getByPositionId(position.getId());
        List<Long> deleteIdsPositionProcess = WebUtils.getDeleteIds(ppRequests, ppDtos);
        positionProcessService.deleteByIds(deleteIdsPositionProcess);

        this.savePositionLevels(plRequests, position.getId());
        this.savePositionProcesses(ppRequests, position.getId());

        return IDResponse.<Long>builder()
                .id(position.getId())
                .build();
    }

    private void savePositionLevels(List<PositionLevelRequest> positionLevelRequests, Long positionId){
        if(Objects.nonNull(positionLevelRequests)){
            List<PositionLevelDto> positionLevels = positionLevelRequests.stream()
                    .map(req -> PositionLevelDto.builder()
                            .id(req.getId())
                            .name(req.getName())
                            .levelId(req.getLevelId())
                            .positionId(positionId)
                            .build()
                    ).collect(Collectors.toList());
            positionLevelService.create(positionLevels);
        }
    }

    private void savePositionProcesses(List<PositionProcessRequest> positionProcessRequests, Long positionId){
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
            AtomicInteger counter = new AtomicInteger(1);
            List<PositionProcessDto> positionProcessDtos = positionProcessRequests.stream()
                    .map(req -> PositionProcessDto.builder()
                            .id(req.getId())
                            .name(req.getName())
                            .processTypeId(req.getProcessTypeId())
                            .positionId(positionId)
                            .sortOrder(counter.getAndIncrement())
                            .build()
                    ).collect(Collectors.toList());
            positionProcessService.create(positionProcessDtos);
        }
    }

}
