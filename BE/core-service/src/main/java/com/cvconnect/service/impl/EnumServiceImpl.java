package com.cvconnect.service.impl;

import com.cvconnect.dto.enums.*;
import com.cvconnect.enums.*;
import com.cvconnect.service.EnumService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumServiceImpl implements EnumService {
    @Override
    public List<CurrencyTypeDto> getCurrencyType() {
        return CurrencyType.getAll();
    }

    @Override
    public List<JobAdStatusDto> getJobAdStatus() {
        return JobAdStatus.getAll();
    }

    @Override
    public List<JobTypeDto> getJobType() {
        return JobType.getAll();
    }

    @Override
    public List<SalaryTypeDto> getSalaryType() {
        return SalaryType.getAll();
    }

    @Override
    public List<EliminateReasonEnumDto> getEliminateReason() {
        return EliminateReasonEnum.getAll();
    }

    @Override
    public List<CalendarTypeDto> getCalendarType() {
        return CalendarType.getAll();
    }
}
