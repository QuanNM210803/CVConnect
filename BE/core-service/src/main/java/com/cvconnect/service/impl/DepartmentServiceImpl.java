package com.cvconnect.service.impl;

import com.cvconnect.common.RestTemplateClient;
import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.entity.Department;
import com.cvconnect.enums.CoreErrorCode;
import com.cvconnect.repository.DepartmentRepository;
import com.cvconnect.service.DepartmentService;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.utils.DateUtils;
import nmquan.commonlib.utils.ObjectMapperUtils;
import nmquan.commonlib.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Override
    @Transactional
    public IDResponse<Long> create(DepartmentRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
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
        Long orgId = restTemplateClient.validOrgMember();
        Optional<Department> department = departmentRepository.findByIdAndOrgId(id, orgId);
        return department.map(value -> ObjectMapperUtils.convertToObject(value, DepartmentDto.class)).orElse(null);
    }

    @Override
    @Transactional
    public void changeStatusActive(ChangeStatusActiveRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
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
        Long orgId = restTemplateClient.validOrgMember();
        List<Department> departments = departmentRepository.findByIdsAndOrgId(ids, orgId);
        if (departments.size() != ids.size()) {
            throw new AppException(CoreErrorCode.DEPARTMENT_NOT_FOUND);
        }
        departmentRepository.deleteAll(departments);
    }

    @Override
    @Transactional
    public IDResponse<Long> update(DepartmentRequest request) {
        Long orgId = restTemplateClient.validOrgMember();
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
        Long orgId = restTemplateClient.validOrgMember();
        request.setOrgId(orgId);
        if (request.getCreatedAtEnd() != null) {
            request.setCreatedAtEnd(DateUtils.endOfDay(request.getCreatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        if (request.getUpdatedAtEnd() != null) {
            request.setUpdatedAtEnd(DateUtils.endOfDay(request.getUpdatedAtEnd(), CommonConstants.ZONE.UTC));
        }
        Page<Department> page = departmentRepository.filter(request, request.getPageable());
        List<DepartmentDto> data = ObjectMapperUtils.convertToList(page.getContent(), DepartmentDto.class);
        return PageUtils.toFilterResponse(page, data);
    }

    @Override
    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if(ObjectUtils.isEmpty(department)) {
            return null;
        }
        return ObjectMapperUtils.convertToObject(department, DepartmentDto.class);
    }
}
