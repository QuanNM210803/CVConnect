package com.cvconnect.service;

import com.cvconnect.dto.ChangeStatusActiveRequest;
import com.cvconnect.dto.EmailTemplateDto;
import com.cvconnect.dto.EmailTemplateFilterRequest;
import com.cvconnect.dto.EmailTemplateRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface EmailTemplateService {
    IDResponse<Long> create(EmailTemplateRequest request);
    FilterResponse<EmailTemplateDto> filter(EmailTemplateFilterRequest request);
    IDResponse<Long> update(EmailTemplateRequest request);
    EmailTemplateDto detail(Long id);
    void delete(List<Long> ids);
    void changeStatusActive(ChangeStatusActiveRequest request);
    List<EmailTemplateDto> getByOrgId(Long orgId);
    EmailTemplateDto getById(Long id);
}
