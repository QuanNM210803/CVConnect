package com.cvconnect.controller;

import com.cvconnect.dto.industry.IndustryDto;
import com.cvconnect.dto.industry.IndustryFilterRequest;
import com.cvconnect.dto.industry.IndustryRequest;
import com.cvconnect.service.IndustryService;
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
@RequestMapping("/industry")
public class IndustryController {
    @Autowired
    private IndustryService industryService;
    @Autowired
    private LocalizationUtils localizationUtils;

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

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('INDUSTRY:VIEW')")
    @Operation(summary = "Get industry detail by id")
    public ResponseEntity<Response<IndustryDto>> getIndustryDetail(@PathVariable Long id) {
        return ResponseUtils.success(industryService.detail(id));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('INDUSTRY:DELETE')")
    @Operation(summary = "Delete industries by ids")
    public ResponseEntity<Response<Void>> deleteIndustries(@RequestBody List<Long> ids) {
        industryService.deleteByIds(ids);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('INDUSTRY:ADD')")
    @Operation(summary = "Create industry")
    public ResponseEntity<Response<IDResponse<Long>>> createIndustry(@Valid @RequestBody IndustryRequest request) {
        return ResponseUtils.success(industryService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('INDUSTRY:UPDATE')")
    @Operation(summary = "Update industry")
    public ResponseEntity<Response<IDResponse<Long>>> updateIndustry(@PathVariable Long id, @Valid @RequestBody IndustryRequest request) {
        request.setId(id);
        return ResponseUtils.success(industryService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }
}
