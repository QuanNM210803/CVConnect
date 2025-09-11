package com.cvconnect.controller;

import com.cvconnect.dto.org.OrganizationRequest;
import com.cvconnect.service.OrgService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.constant.CommonConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @InternalRequest
    @PostMapping("/internal/create")
    @Operation(summary = "Create Organization")
    public ResponseEntity<Response<IDResponse<Long>>> createOrg(@Valid @RequestPart("data") OrganizationRequest data,
                                                                    @RequestPart("files") MultipartFile[] files) {
        return ResponseUtils.success(orgService.createOrg(data, files));
    }
}
