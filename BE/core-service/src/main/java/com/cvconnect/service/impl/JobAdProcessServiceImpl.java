package com.cvconnect.service.impl;

import com.cvconnect.dto.jobAd.JobAdProcessDto;
import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.entity.JobAdProcess;
import com.cvconnect.repository.JobAdProcessRepository;
import com.cvconnect.service.JobAdProcessService;
import com.cvconnect.service.ProcessTypeService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobAdProcessServiceImpl implements JobAdProcessService {
    @Autowired
    private JobAdProcessRepository jobAdProcessRepository;
    @Autowired
    private ProcessTypeService processTypeService;

    @Override
    public void create(List<JobAdProcessDto> dtos) {
        List<JobAdProcess> entities = ObjectMapperUtils.convertToList(dtos, JobAdProcess.class);
        if(!entities.isEmpty()){
            jobAdProcessRepository.saveAll(entities);
        }
    }

    @Override
    public List<JobAdProcessDto> getByJobAdId(Long jobAdId) {
        List<JobAdProcess> entities = jobAdProcessRepository.findByJobAdId(jobAdId);
        if(entities.isEmpty()){
            return List.of();
        }
        List<JobAdProcessDto> dtos = ObjectMapperUtils.convertToList(entities, JobAdProcessDto.class);
        List<ProcessTypeDto> processTypeDtos = processTypeService.getAllProcessType();
        Map<Long, ProcessTypeDto> processTypeMap = processTypeDtos.stream()
                .collect(Collectors.toMap(ProcessTypeDto::getId, Function.identity()));
        dtos.forEach(dto -> dto.setProcessType(processTypeMap.get(dto.getProcessTypeId())));
        return dtos;
    }

    @Override
    public JobAdProcessDto getById(Long id) {
        JobAdProcess entity = jobAdProcessRepository.findById(id).orElse(null);
        if(ObjectUtils.isEmpty(entity)){
            return null;
        }
        JobAdProcessDto jobAdProcessDto = ObjectMapperUtils.convertToObject(entity, JobAdProcessDto.class);
        List<ProcessTypeDto> processTypeDtos = processTypeService.getAllProcessType();
        for(ProcessTypeDto dto : processTypeDtos){
            if(dto.getId().equals(jobAdProcessDto.getProcessTypeId())){
                jobAdProcessDto.setProcessType(dto);
                break;
            }
        }
        return jobAdProcessDto;
    }
}
