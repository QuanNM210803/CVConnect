package com.cvconnect.service.impl;

import com.cvconnect.dto.common.ChangeStatusActiveRequest;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.entity.Department;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.DepartmentRepository;
import com.cvconnect.service.DepartmentService;
import com.cvconnect.utils.CoreServiceUtils;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public IDResponse<Long> create(DepartmentRequest request) {
        Long orgId = CoreServiceUtils.getCurrentOrgId();
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
        Long orgId = CoreServiceUtils.getCurrentOrgId();
        Optional<Department> department = departmentRepository.findByIdAndOrgId(id, orgId);
        return department.map(value -> ObjectMapperUtils.convertToObject(value, DepartmentDto.class)).orElse(null);
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        Long orgId = CoreServiceUtils.getCurrentOrgId();
        List<Department> departments = departmentRepository.findByIdsAndOrgId(request.getIds(), orgId);
        if (departments.size() != request.getIds().size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        departments.forEach(department -> department.setIsActive(request.getActive()));
        departmentRepository.saveAll(departments);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        Long orgId = CoreServiceUtils.getCurrentOrgId();
        List<Department> departments = departmentRepository.findByIdsAndOrgId(ids, orgId);
        if (departments.size() != ids.size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        departmentRepository.deleteAll(departments);
    }

    @Override
    @Transactional
    public IDResponse<Long> update(DepartmentRequest request) {
        Long orgId = CoreServiceUtils.getCurrentOrgId();
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndOrgId(request.getId(), orgId);
        if (optionalDepartment.isEmpty()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }

        Department department = optionalDepartment.get();
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
        Long orgId = CoreServiceUtils.getCurrentOrgId();
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
}
