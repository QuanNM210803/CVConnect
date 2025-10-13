package com.cvconnect.service;

import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.dto.EmailConfigRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface EmailConfigService {
    EmailConfigDto getByOrgId(Long orgId);
    EmailConfigDto detail();
    IDResponse<Long> create(EmailConfigRequest request);
    IDResponse<Long> update(EmailConfigRequest request);
    void delete();
}
