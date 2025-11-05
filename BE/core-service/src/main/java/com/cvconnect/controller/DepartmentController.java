package com.cvconnect.controller;

import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.department.DepartmentFilterRequest;
import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:ADD')")
    @Operation(summary = "Create a new department")
    public ResponseEntity<Response<IDResponse<Long>>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        return ResponseUtils.success(departmentService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:VIEW')")
    @Operation(summary = "Get department detail by id")
    public ResponseEntity<Response<DepartmentDto>> getDepartmentDetail(@PathVariable Long id) {
        return ResponseUtils.success(departmentService.detail(id));
    }

    @PutMapping("/change-status-active")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:UPDATE')")
    @Operation(summary = "Change status active department")
    public ResponseEntity<Response<Boolean>> changeStatusActive(@Valid @RequestBody ChangeStatusActiveRequest request) {
        departmentService.changeStatusActive(request);
        return ResponseUtils.success(true, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:DELETE')")
    @Operation(summary = "Delete departments by ids")
    public ResponseEntity<Response<Boolean>> deleteDepartments(@RequestBody List<Long> ids) {
        departmentService.deleteByIds(ids);
        return ResponseUtils.success(true, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:UPDATE')")
    @Operation(summary = "Update a department")
    public ResponseEntity<Response<IDResponse<Long>>> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        request.setId(id);
        return ResponseUtils.success(departmentService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('DEPARTMENT:VIEW', 'ORGANIZATION')")
    @Operation(summary = "Filter departments")
    public ResponseEntity<Response<FilterResponse<DepartmentDto>>> filterDepartments(@Valid @ModelAttribute DepartmentFilterRequest request) {
        return ResponseUtils.success(departmentService.filter(request));
    }
}
