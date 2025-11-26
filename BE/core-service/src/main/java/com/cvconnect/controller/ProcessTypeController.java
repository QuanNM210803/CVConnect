package com.cvconnect.controller;

import com.cvconnect.dto.processType.ProcessTypeDto;
import com.cvconnect.dto.processType.ProcessTypeRequest;
import com.cvconnect.service.ProcessTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process-type")
public class ProcessTypeController {
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/detail/{id}")
    @Operation(summary = "Get process type detail by id")
    @PreAuthorize("hasAnyAuthority('PROCESS_TYPE:VIEW')")
    public ResponseEntity<Response<ProcessTypeDto>> getProcessTypeDetail(@PathVariable Long id) {
        return ResponseUtils.success(processTypeService.detail(id));
    }

    @PostMapping("/update")
    @Operation(summary = "Create or update, delete process type")
    @PreAuthorize("hasAnyAuthority('PROCESS_TYPE:ADD', 'PROCESS_TYPE:UPDATE', 'PROCESS_TYPE:DELETE')")
    public ResponseEntity<Response<Void>> changeProcessType(@Valid @RequestBody List<ProcessTypeRequest> request) {
        processTypeService.changeProcessType(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get all process type")
    @PreAuthorize("hasAnyAuthority('PROCESS_TYPE:VIEW', 'ORG_ADMIN')")
    public ResponseEntity<Response<List<ProcessTypeDto>>> getAllProcessType() {
        return ResponseUtils.success(processTypeService.getAllProcessType());
    }
}
