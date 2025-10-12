package com.cvconnect.utils;

import com.cvconnect.dto.orgMember.OrgMemberDto;
import com.cvconnect.enums.UserErrorCode;
import com.cvconnect.service.OrgMemberService;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {
    @Lazy
    @Autowired
    private OrgMemberService orgMemberService;

    public Long validOrgMember() {
        Long orgId = WebUtils.checkCurrentOrgId();
        OrgMemberDto orgMember = orgMemberService.getOrgMember(WebUtils.getCurrentUserId());
        if (orgMember == null || !orgMember.getOrgId().equals(orgId)) {
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        if (!orgMember.getIsActive()) {
            throw new AppException(UserErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        return orgId;
    }
}
