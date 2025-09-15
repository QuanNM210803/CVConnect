package com.cvconnect.service;

import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.dto.processType.ProcessTypeRequest;

import java.util.List;

public interface ProcessTypeService {
    ProcessTypeDto detail(Long id);
    void changeProcessType(List<ProcessTypeRequest> request);
    List<ProcessTypeDto> getAllProcessType();
}
