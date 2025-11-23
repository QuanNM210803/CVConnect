package com.cvconnect.controller;

import com.cvconnect.dto.org.OrgDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @InternalRequest
    @GetMapping("/internal/get-by-id/{orgId}")
    @Operation(summary = "Get Organization by id")
    public ResponseEntity<Response<OrgDto>> createOrg(@PathVariable Long orgId) {
        return ResponseUtils.success(orgService.findById(orgId));
    }

    @GetMapping("/org-info")
    @PreAuthorize("hasAnyAuthority('ORG_INFO:VIEW')")
    @Operation(summary = "Get Organization info")
    public ResponseEntity<Response<OrgDto>> getOrgInfo() {
        return ResponseUtils.success(orgService.getOrgInfo());
    }

    @PutMapping("/update-info")
    @PreAuthorize("hasAnyAuthority('ORG_INFO:UPDATE')")
    @Operation(summary = "Update Organization info")
    public ResponseEntity<Response<IDResponse<Long>>> updateOrgInfo(@Valid @RequestBody OrganizationRequest data){
        return ResponseUtils.success(orgService.updateOrgInfo(data));
    }

    @PutMapping("/update-logo")
    @PreAuthorize("hasAnyAuthority('ORG_INFO:UPDATE')")
    @Operation(summary = "Update Organization logo")
    public ResponseEntity<Response<IDResponse<Long>>> updateOrgLogo(@RequestPart("file") MultipartFile file) {
        return ResponseUtils.success(orgService.updateOrgLogo(file));
    }

    @PutMapping("/update-cover-photo")
    @PreAuthorize("hasAnyAuthority('ORG_INFO:UPDATE')")
    @Operation(summary = "Update Organization cover photo")
    public ResponseEntity<Response<IDResponse<Long>>> updateOrgCoverPhoto(@RequestPart("file") MultipartFile file) {
        return ResponseUtils.success(orgService.updateOrgCoverPhoto(file));
    }

    @GetMapping("/outside/org-info/{orgId}")
    @Operation(summary = "Get Organization info outside")
    public ResponseEntity<Response<OrgDto>> getOrgInfoOutside(@PathVariable Long orgId) {
        return ResponseUtils.success(orgService.getOrgInfoOutside(orgId));
    }

    @GetMapping("/outside/org-featured")
    @Operation(summary = "Get Featured Organizations outside")
    public ResponseEntity<Response<List<OrgDto>>> getFeaturedOrgOutside() {
        return ResponseUtils.success(orgService.getFeaturedOrgOutside());
    }

    @GetMapping("/outside/org-by-job-ad/{jobAdId}")
    @Operation(summary = "Get Organization by Job Ad id outside")
    public ResponseEntity<Response<OrgDto>> getOrgByJobAd(@PathVariable Long jobAdId) {
        return ResponseUtils.success(orgService.getOrgByJobAd(jobAdId));
    }
}
