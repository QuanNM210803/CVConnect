package com.cvconnect.service;

import com.cvconnect.dto.candidateSummaryOrg.CandidateSummaryOrgRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface CandidateSummaryOrgService {
    IDResponse<Long> saveSummary(CandidateSummaryOrgRequest request);
}
