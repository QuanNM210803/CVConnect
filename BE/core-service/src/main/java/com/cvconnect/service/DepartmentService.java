package com.cvconnect.service;

import com.cvconnect.dto.common.ChangeStatusActiveRequest;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.dto.department.DepartmentRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;

import java.util.List;

public interface DepartmentService {
    IDResponse<Long> create(DepartmentRequest request);
    DepartmentDto detail(Long id);
    void changeStatusActive(ChangeStatusActiveRequest request);
    void deleteByIds(List<Long> ids);
    IDResponse<Long> update(DepartmentRequest request);
    FilterResponse<DepartmentDto> filter(DepartmentFilterRequest request);
}
