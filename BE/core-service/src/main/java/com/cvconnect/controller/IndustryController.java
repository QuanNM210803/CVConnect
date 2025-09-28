package com.cvconnect.controller;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.service.IndustryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/industry")
public class IndustryController {
    @Autowired
    private IndustryService industryService;

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('INDUSTRY:VIEW')")
    @Operation(summary = "Filter industries")
    public ResponseEntity<Response<FilterResponse<IndustryDto>>> filterIndustries(@Valid @ModelAttribute IndustryFilterRequest request) {
        return ResponseUtils.success(industryService.filter(request));
    }

    @GetMapping("/public/filter")
    @Operation(summary = "Filter public industries")
    public ResponseEntity<Response<FilterResponse<IndustryDto>>> filterPublicIndustries(@Valid @ModelAttribute IndustryFilterRequest request) {
        return ResponseUtils.success(industryService.filterPublic(request));
    }
}
