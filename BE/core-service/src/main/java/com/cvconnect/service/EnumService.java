package com.cvconnect.service;

import com.cvconnect.dto.jobAd.CurrencyTypeDto;
import com.cvconnect.dto.jobAd.JobAdStatusDto;
import com.cvconnect.dto.jobAd.JobTypeDto;
import com.cvconnect.dto.jobAd.SalaryTypeDto;

import java.util.List;

public interface EnumService {
    List<CurrencyTypeDto> getCurrencyType();
    List<JobAdStatusDto> getJobAdStatus();
    List<JobTypeDto> getJobType();
    List<SalaryTypeDto> getSalaryType();
}
