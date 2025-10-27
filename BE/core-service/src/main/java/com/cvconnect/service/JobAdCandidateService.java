package com.cvconnect.service;

import com.cvconnect.dto.candidateInfoApply.CandidateInfoDetail;
import com.cvconnect.dto.jobAdCandidate.*;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import org.springframework.web.multipart.MultipartFile;

public interface JobAdCandidateService {
    IDResponse<Long> apply(ApplyRequest request, MultipartFile cvFile);
    FilterResponse<CandidateFilterResponse> filter(CandidateFilterRequest request);
    CandidateInfoDetail candidateDetail(Long candidateInfoId);
    boolean checkCandidateInfoInOrg(Long candidateInfoId, Long orgId, Long hrContactId);
    void changeCandidateProcess(ChangeCandidateProcessRequest request);
    void eliminateCandidate(EliminateCandidateRequest request);
    void changeOnboardDate(ChangeOnboardDateRequest request);
    void markOnboard(MarkOnboardRequest request);
    Boolean existsByJobAdCandidateIdAndHrContactId(Long jobAdCandidateId, Long hrContactId);
    Boolean existsByJobAdCandidateIdAndOrgId(Long jobAdCandidateId, Long orgId);
    JobAdCandidateDto findById(Long jobAdCandidateId);

}
