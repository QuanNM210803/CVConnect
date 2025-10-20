package com.cvconnect.service;

import com.cvconnect.dto.jobAdCandidate.ApplyRequest;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterRequest;
import com.cvconnect.dto.jobAdCandidate.CandidateFilterResponse;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import org.springframework.web.multipart.MultipartFile;

public interface JobAdCandidateService {
    IDResponse<Long> apply(ApplyRequest request, MultipartFile cvFile);
    FilterResponse<CandidateFilterResponse> filter(CandidateFilterRequest request);

}
