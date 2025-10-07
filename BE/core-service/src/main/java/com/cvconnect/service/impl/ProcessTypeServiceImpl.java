package com.cvconnect.service.impl;

import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.dto.processType.ProcessTypeRequest;
import com.cvconnect.entity.ProcessType;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.ProcessTypeRepository;
import com.cvconnect.service.ProcessTypeService;
import nmquan.commonlib.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessTypeServiceImpl implements ProcessTypeService {
    @Autowired
    private ProcessTypeRepository processTypeRepository;

    @Override
    public ProcessTypeDto detail(Long id) {
        ProcessType processType = processTypeRepository.findById(id).orElse(null);
        if (processType == null) {
            return null;
        }
        return ProcessTypeDto.builder()
                .id(processType.getId())
                .name(processType.getName())
                .code(processType.getCode())
                .sortOrder(processType.getSortOrder())
                .isDefault(processType.getIsDefault())
                .build();
    }

    @Override
    @Transactional
    public void changeProcessType(List<ProcessTypeRequest> request) {
        List<Long> idsInRequest = request.stream()
                .map(ProcessTypeRequest::getId)
                .toList();

        List<ProcessType> processTypes = new ArrayList<>(processTypeRepository.findAll());
        List<Long> idsDefault = processTypes.stream()
                .filter(ProcessType::getIsDefault)
                .map(ProcessType::getId)
                .toList();

        List<Long> idsInDb = new ArrayList<>(processTypes.stream()
                .map(ProcessType::getId)
                .toList());

        // Delete: đã dùng thì không cho xoá và vòng mặc định không cho xoá
        List<Long> idsToDelete = idsInDb.stream()
                .filter(id -> !idsInRequest.contains(id))
                .toList();
        for(Long id : idsToDelete) {
            if (idsDefault.contains(id)) {
                throw new AppException(CoreErrorCode.PROCESS_TYPE_CANNOT_DELETE_DEFAULT);
            }
        }
        processTypeRepository.deleteAllById(idsToDelete);
        processTypes.removeIf(pt -> idsToDelete.contains(pt.getId()));
        idsInDb.removeIf(idsToDelete::contains);

        List<String> codeExist = new ArrayList<>(processTypes.stream()
                .map(ProcessType::getCode)
                .toList());
        // Insert
        List<ProcessType> toInsert = request.stream()
                .filter(r -> r.getId() == null)
                .map(r -> {
                    if (codeExist.contains(r.getCode())) {
                        throw new AppException(CoreErrorCode.PROCESS_TYPE_CODE_DUPLICATED, r.getCode());
                    }
                    ProcessType pt = new ProcessType();
                    pt.setCode(r.getCode());
                    pt.setName(r.getName());
                    pt.setSortOrder(r.getSortOrder());
                    pt.setIsDefault(false);

                    codeExist.add(r.getCode());
                    processTypes.add(pt);
                    return pt;
                })
                .toList();
        processTypeRepository.saveAll(toInsert);

        // Update
        List<ProcessType> toUpdate = request.stream()
                .filter(r -> r.getId() != null && idsInDb.contains(r.getId()))
                .map(type -> {
                    ProcessType pt = processTypes.stream()
                            .filter(t -> t.getId().equals(type.getId()))
                            .findFirst()
                            .orElseThrow(() -> new AppException(CoreErrorCode.PROCESS_TYPE_NOT_FOUND));
                    if (!pt.getCode().equals(type.getCode()) && codeExist.contains(type.getCode())) {
                        throw new AppException(CoreErrorCode.PROCESS_TYPE_CODE_DUPLICATED, type.getCode());
                    }
                    codeExist.add(type.getCode());
                    pt.setName(type.getName());
                    pt.setSortOrder(type.getSortOrder());
                    return pt;
                })
                .toList();
        processTypeRepository.saveAll(toUpdate);
    }

    @Override
    public List<ProcessTypeDto> getAllProcessType() {
        return processTypeRepository.findAll().stream()
                .map(type -> ProcessTypeDto.builder()
                        .id(type.getId())
                        .name(type.getName())
                        .code(type.getCode())
                        .sortOrder(type.getSortOrder())
                        .isDefault(type.getIsDefault())
                        .build()
                )
                .sorted(Comparator.comparing(ProcessTypeDto::getSortOrder))
                .collect(Collectors.toList());
    }
}
