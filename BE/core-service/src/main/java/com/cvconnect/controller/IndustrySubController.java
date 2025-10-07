package com.cvconnect.controller;

import com.cvconnect.dto.industrySub.IndustrySubDto;
import com.cvconnect.dto.industrySub.IndustrySubFilterRequest;
import com.cvconnect.service.IndustrySubService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/industry-sub")
public class IndustrySubController {
    @Autowired
    private IndustrySubService industrySubService;

    @GetMapping("/filter")
    @Operation(summary = "Filter Industry Sub")
    public ResponseEntity<Response<FilterResponse<IndustrySubDto>>> filterIndustrySub(@Valid @ModelAttribute IndustrySubFilterRequest request) {
        return ResponseUtils.success(industrySubService.filter(request));
    }
}
