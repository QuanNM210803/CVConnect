package com.cvconnect.controller;

import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.dto.EmailConfigRequest;
import com.cvconnect.service.EmailConfigService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email-config")
public class EmailConfigController {
    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/get-by-org")
    @Operation(summary = "Get email config by organization ID")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN')")
    public ResponseEntity<Response<EmailConfigDto>> getByOrgId() {
        return ResponseUtils.success(emailConfigService.detail());
    }

    @InternalRequest
    @GetMapping("/internal/get-by-org")
    @Operation(summary = "Get email config by organization ID")
    public ResponseEntity<Response<EmailConfigDto>> getByOrgIdInternal() {
        return ResponseUtils.success(emailConfigService.detail());
    }

    @PostMapping("/create")
    @Operation(summary = "Create email config for organization")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN')")
    public ResponseEntity<Response<IDResponse<Long>>> createEmailConfig(@RequestBody EmailConfigRequest request) {
        return ResponseUtils.success(emailConfigService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update email config for organization")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN')")
    public ResponseEntity<Response<IDResponse<Long>>> updateEmailConfig(@PathVariable Long id,
                                                                        @RequestBody EmailConfigRequest request) {
        request.setId(id);
        return ResponseUtils.success(emailConfigService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete email config for organization")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN')")
    public ResponseEntity<Response<Void>> deleteEmailConfig(@PathVariable Long id) {
        emailConfigService.delete(id);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }
}
