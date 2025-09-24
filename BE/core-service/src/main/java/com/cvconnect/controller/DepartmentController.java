package com.cvconnect.controller;

import com.cvconnect.dto.department.DepartmentRequest;
import com.cvconnect.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
