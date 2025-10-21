package com.cvconnect.controller;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import com.cvconnect.service.CareerService;
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
@RequestMapping("/career")
public class CareerController {
    @Autowired
    private CareerService careerService;

    @GetMapping("/filter")
    @Operation(summary = "Filter Industry Sub")
    public ResponseEntity<Response<FilterResponse<CareerDto>>> filterIndustrySub(@Valid @ModelAttribute CareerFilterRequest request) {
        return ResponseUtils.success(careerService.filter(request));
    }
}
