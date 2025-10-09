package com.cvconnect.controller;

import com.cvconnect.dto.EmailConfigDto;
import com.cvconnect.service.EmailConfigService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-config")
public class EmailConfigController {
    @Autowired
    private EmailConfigService emailConfigService;

    @GetMapping("/get-by-org")
    @Operation(summary = "Get email config by organization ID")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN')")
    public ResponseEntity<Response<EmailConfigDto>> getByOrgId() {
        return ResponseUtils.success(emailConfigService.getByOrgId(null));
    }

    @InternalRequest
    @GetMapping("/internal/get-by-org")
    @Operation(summary = "Get email config by organization ID")
    public ResponseEntity<Response<EmailConfigDto>> getByOrgIdInternal() {
        return ResponseUtils.success(emailConfigService.getByOrgId(null));
    }
}
