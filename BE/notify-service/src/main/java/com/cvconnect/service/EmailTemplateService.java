package com.cvconnect.service;

import com.cvconnect.dto.*;
import com.cvconnect.dto.internal.request.DataReplacePlaceholder;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
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
    EmailTemplateDto previewEmail(Long id, DataReplacePlaceholder dataReplacePlaceholder);
    EmailTemplateDto previewEmail(PreviewEmailWithoutTemplate request);
    String previewEmailDefault(PreviewEmailDefaultRequest request);
}
