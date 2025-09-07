package com.cvconnect.controller;

import com.cvconnect.dto.org.OrganizationRequest;
import com.cvconnect.service.OrgService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @InternalRequest
    @PostMapping("/internal/create")
    @Operation(summary = "Create Organization - Internal API")
    public ResponseEntity<Response<IDResponse<Long>>> createOrg(@Valid @RequestBody OrganizationRequest request) {
        return ResponseUtils.success(orgService.createOrg(request));
    }
}
