package com.cvconnect.controller;

import com.cvconnect.dto.ChangeStatusActiveRequest;
import com.cvconnect.dto.EmailTemplateDto;
import com.cvconnect.dto.EmailTemplateFilterRequest;
import com.cvconnect.dto.EmailTemplateRequest;
import com.cvconnect.service.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
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
@RequestMapping("/email-template")
public class EmailTemplateController {
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @Operation(summary = "Create Email Template")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:ADD')")
    public ResponseEntity<Response<IDResponse<Long>>> createEmailTemplate(@Valid @RequestBody EmailTemplateRequest request) {
        return ResponseUtils.success(emailTemplateService.create(request),
                localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter Email Templates")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:VIEW')")
    public ResponseEntity<Response<FilterResponse<EmailTemplateDto>>> filterEmailTemplates(@Valid @ModelAttribute EmailTemplateFilterRequest request) {
        return ResponseUtils.success(emailTemplateService.filter(request));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Get Email Template Detail")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:VIEW')")
    public ResponseEntity<Response<EmailTemplateDto>> detail(@PathVariable Long id) {
        return ResponseUtils.success(emailTemplateService.detail(id));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Email Template")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:UPDATE')")
    public ResponseEntity<Response<IDResponse<Long>>> updateEmailTemplate(@PathVariable Long id,
                                                                          @Valid @RequestBody EmailTemplateRequest request) {
        request.setId(id);
        return ResponseUtils.success(emailTemplateService.update(request),
                localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @PutMapping("change-status-active")
    @Operation(summary = "Change Status Active Email Template")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:UPDATE')")
    public ResponseEntity<Response<Void>> changeStatusActive(@Valid @RequestBody ChangeStatusActiveRequest request) {
        emailTemplateService.changeStatusActive(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Email Templates")
    @PreAuthorize("hasAnyAuthority('EMAIL_TEMPLATE:DELETE')")
    public ResponseEntity<Response<Void>> deleteEmailTemplates(@RequestBody List<Long> ids) {
        emailTemplateService.delete(ids);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }
}
