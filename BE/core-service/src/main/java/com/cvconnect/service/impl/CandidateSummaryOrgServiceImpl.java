package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.constant.Constants;
import com.cvconnect.dto.candidateSummaryOrg.CandidateSummaryOrgRequest;
import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.entity.CandidateSummaryOrg;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.CandidateSummaryOrgRepository;
import com.cvconnect.service.CandidateSummaryOrgService;
import com.cvconnect.service.JobAdCandidateService;
import com.cvconnect.service.LevelService;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CandidateSummaryOrgServiceImpl implements CandidateSummaryOrgService {
    @Autowired
    private CandidateSummaryOrgRepository candidateSummaryOrgRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
    private JobAdCandidateService jobAdCandidateService;
    @Autowired
    private LevelService levelService;

    @Override
    public IDResponse<Long> saveSummary(CandidateSummaryOrgRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
        Long hrContactId = null;
        List<String> role = WebUtils.getCurrentRole();
        if(!role.contains(Constants.RoleCode.ORG_ADMIN)){
            hrContactId = WebUtils.getCurrentUserId();
        }

        boolean checkAuthority = jobAdCandidateService.checkCandidateInfoInOrg(request.getCandidateInfoId(), orgId, hrContactId);
        if(!checkAuthority){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }

        CandidateSummaryOrg candidateSummaryOrg = candidateSummaryOrgRepository.findByCandidateInfoIdAndOrgId(request.getCandidateInfoId(), orgId);
        if(ObjectUtils.isEmpty(candidateSummaryOrg)) {
            candidateSummaryOrg = new CandidateSummaryOrg();
            candidateSummaryOrg.setCandidateInfoId(request.getCandidateInfoId());
            candidateSummaryOrg.setOrgId(orgId);
        }

        if(!ObjectUtils.isEmpty(request.getLevelId())){
            LevelDto levelDto = levelService.getById(request.getLevelId());
            if(ObjectUtils.isEmpty(levelDto)){
                throw new AppException(CoreErrorCode.LEVEL_NOT_FOUND);
            }
            candidateSummaryOrg.setLevelId(request.getLevelId());
        }
        if(!ObjectUtils.isEmpty(request.getSkill())){
            candidateSummaryOrg.setSkill(request.getSkill());
        }
        candidateSummaryOrgRepository.save(candidateSummaryOrg);

        return IDResponse.<Long>builder()
                .id(candidateSummaryOrg.getId())
                .build();
    }
}
