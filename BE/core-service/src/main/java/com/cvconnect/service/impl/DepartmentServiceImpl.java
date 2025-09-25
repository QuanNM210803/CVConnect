package com.cvconnect.service.impl;

import com.cvconnect.constant.Constants;
import com.cvconnect.dto.ChangeStatusActiveRequest;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.entity.Department;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.DepartmentRepository;
import com.cvconnect.service.DepartmentService;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import nmquan.commonlib.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
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

    @Override
    public DepartmentDto detail(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isEmpty()) {
            return null;
        }
        this.checkDataAuthorization(null, department.stream().toList());
        return ObjectMapperUtils.convertToObject(department.get(), DepartmentDto.class);
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        List<Department> departments = departmentRepository.findAllById(request.getIds());
        if (departments.size() != request.getIds().size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        this.checkDataAuthorization(null, departments);
        departments.forEach(department -> department.setIsActive(request.getActive()));
        departmentRepository.saveAll(departments);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        List<Department> departments = departmentRepository.findAllById(ids);
        if (departments.size() != ids.size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        this.checkDataAuthorization(null, departments);
        departmentRepository.deleteAll(departments);
    }

    @Override
    @Transactional
    public IDResponse<Long> update(DepartmentRequest request) {
        Long orgId = WebUtils.getCurrentOrgId();
        if(Objects.isNull(orgId)){
            throw new AppException(CoreErrorCode.DEPARTMENT_ORG_ID_REQUIRE);
        }

        Optional<Department> optionalDepartment = departmentRepository.findById(request.getId());
        if (optionalDepartment.isEmpty()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        Department department = optionalDepartment.get();
        this.checkDataAuthorization(orgId, List.of(department));

        boolean exists = departmentRepository.existsByCodeAndOrgId(request.getCode(), orgId);
        if (exists && !department.getCode().equals(request.getCode())) {
            throw new AppException(CoreErrorCode.DEPARTMENT_CODE_DUPLICATED, request.getCode());
        }
        department.setName(request.getName());
        department.setCode(request.getCode());
        departmentRepository.save(department);
        return IDResponse.<Long>builder()
                .id(department.getId())
                .build();
    }

    @Override
    public FilterResponse<DepartmentDto> filter(DepartmentFilterRequest request) {
        Long orgId = WebUtils.getCurrentOrgId();
        if(Objects.isNull(orgId)){
            throw new AppException(CoreErrorCode.DEPARTMENT_ORG_ID_REQUIRE);
        }
        request.setOrgId(orgId);
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(request.getCreatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(request.getUpdatedAtEnd().plus(1, ChronoUnit.DAYS));
        }
        Page<Department> page = departmentRepository.filter(request, request.getPageable());
        List<DepartmentDto> data = ObjectMapperUtils.convertToList(page.getContent(), DepartmentDto.class);
        return PageUtils.toFilterResponse(page, data);
    }

    void checkDataAuthorization(Long orgId, List<Department> departments) {
        if(Objects.isNull(orgId)){
            orgId = WebUtils.getCurrentOrgId();
        }
        for (Department department : departments) {
            if (!department.getOrgId().equals(orgId)) {
                throw new AppException(CommonErrorCode.ACCESS_DENIED);
            }
        }
    }
}
