package com.cvconnect.service;

import com.cvconnect.dto.jobAdCandidate.ApplyRequest;
import nmquan.commonlib.dto.response.IDResponse;
import org.springframework.web.multipart.MultipartFile;

public interface JobAdCandidateService {
    IDResponse<Long> apply(ApplyRequest request, MultipartFile cvFile);

}
