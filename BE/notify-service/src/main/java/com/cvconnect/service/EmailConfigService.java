package com.cvconnect.service;

import com.cvconnect.dto.EmailConfigDto;

public interface EmailConfigService {
    EmailConfigDto getByOrgId(Long orgId);
}
