package com.cvconnect.service;

import com.cvconnect.dto.department.DepartmentRequest;
import nmquan.commonlib.dto.response.IDResponse;

public interface DepartmentService {
    IDResponse<Long> create(DepartmentRequest request);
}
