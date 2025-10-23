package com.cvconnect.service;

import com.cvconnect.dto.enums.*;

import java.util.List;

public interface EnumService {
    List<CurrencyTypeDto> getCurrencyType();
    List<JobAdStatusDto> getJobAdStatus();
    List<JobTypeDto> getJobType();
    List<SalaryTypeDto> getSalaryType();
    List<EliminateReasonEnumDto> getEliminateReason();
}
