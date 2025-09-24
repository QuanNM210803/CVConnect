package com.cvconnect.service.impl;

import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.entity.Department;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.DepartmentRepository;
import com.cvconnect.service.DepartmentService;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public IDResponse<Long> create(DepartmentRequest request) {
        Long orgId = WebUtils.getCurrentOrgId();
        if(Objects.isNull(orgId)){
            throw new AppException(CoreErrorCode.DEPARTMENT_ORG_ID_REQUIRE);
        }
        boolean exists = departmentRepository.existsByCodeAndOrgId(request.getCode(), orgId);
        if (exists) {
            throw new AppException(CoreErrorCode.DEPARTMENT_CODE_DUPLICATED, request.getCode());
        }
        Department department = new Department();
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setOrgId(orgId);
        departmentRepository.save(department);
        return IDResponse.<Long>builder()
                .id(department.getId())
                .build();
    }
}
