package com.cvconnect.service.impl;

import com.cvconnect.dto.enums.CurrencyTypeDto;
import com.cvconnect.dto.enums.JobAdStatusDto;
import com.cvconnect.dto.enums.JobTypeDto;
import com.cvconnect.dto.enums.SalaryTypeDto;
import com.cvconnect.enums.CurrencyType;
import com.cvconnect.enums.JobAdStatus;
import com.cvconnect.enums.JobType;
import com.cvconnect.enums.SalaryType;
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
}
