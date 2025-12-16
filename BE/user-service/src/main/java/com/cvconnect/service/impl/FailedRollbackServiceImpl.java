package com.cvconnect.service.impl;

import com.cvconnect.dto.failedRollback.FailedRollbackDto;
import com.cvconnect.entity.FailedRollback;
import com.cvconnect.repository.FailedRollbackRepository;
import com.cvconnect.service.FailedRollbackService;
import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class FailedRollbackServiceImpl implements FailedRollbackService {
    @Autowired
    private FailedRollbackRepository failedRollbackRepository;

    @Override
    public void save(FailedRollbackDto failedRollbackDto) {
        FailedRollback failedRollback = ObjectMapperUtils.convertToObject(failedRollbackDto, FailedRollback.class);
        failedRollbackRepository.save(failedRollback);
    }

    @Override
    public List<FailedRollbackDto> getPendingFailedRollbacks() {
        List<FailedRollback> failedRollbacks = failedRollbackRepository.getPendingFailedRollbacks();
        if(ObjectUtils.isEmpty(failedRollbacks)){
            return List.of();
        }
        return ObjectMapperUtils.convertToList(failedRollbacks, FailedRollbackDto.class);
    }
}
