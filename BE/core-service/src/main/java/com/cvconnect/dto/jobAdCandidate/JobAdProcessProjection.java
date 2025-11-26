package com.cvconnect.dto.jobAdCandidate;

import nmquan.commonlib.dto.BaseRepositoryDto;

public interface JobAdProcessProjection extends BaseRepositoryDto {
    String getProcessName();
    Long getJobAdId();
    Integer getSortOrder();
    Long getNumberOfApplicants();
}
