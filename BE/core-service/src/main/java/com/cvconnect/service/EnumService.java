package com.cvconnect.service;

import com.cvconnect.dto.enums.CurrencyTypeDto;
import com.cvconnect.dto.enums.JobAdStatusDto;
import com.cvconnect.dto.enums.JobTypeDto;
import com.cvconnect.dto.enums.SalaryTypeDto;

import java.util.List;

public interface EnumService {
    List<CurrencyTypeDto> getCurrencyType();
    List<JobAdStatusDto> getJobAdStatus();
    List<JobTypeDto> getJobType();
    List<SalaryTypeDto> getSalaryType();
}
