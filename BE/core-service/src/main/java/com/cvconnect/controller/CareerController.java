package com.cvconnect.controller;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.career.CareerFilterRequest;
import com.cvconnect.dto.career.CareerRequest;
import com.cvconnect.service.CareerService;
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
@RequestMapping("/career")
public class CareerController {
    @Autowired
    private CareerService careerService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/filter")
    @Operation(summary = "Filter Career")
    public ResponseEntity<Response<FilterResponse<CareerDto>>> filterIndustrySub(@Valid @ModelAttribute CareerFilterRequest request) {
        return ResponseUtils.success(careerService.filter(request));
    }

    @GetMapping("/detail/{careerId}")
    @Operation(summary = "Get Career Detail")
    @PreAuthorize("hasAnyAuthority('CAREER:VIEW')")
    public ResponseEntity<Response<CareerDto>> getCareerDetail(@PathVariable Long careerId) {
        return ResponseUtils.success(careerService.getCareerDetail(careerId));
    }

    @DeleteMapping("/delete-by-ids")
    @Operation(summary = "Delete Careers by Ids")
    @PreAuthorize("hasAnyAuthority('CAREER:DELETE')")
    public ResponseEntity<Response<Void>> deleteByIds(@RequestBody List<Long> deleteIds) {
        careerService.deleteByIds(deleteIds);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }

    @PostMapping("/create")
    @Operation(summary = "Create Careers")
    @PreAuthorize("hasAnyAuthority('CAREER:ADD')")
    public ResponseEntity<Response<IDResponse<Long>>> create(@RequestBody CareerRequest request) {
        return ResponseUtils.success(careerService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PutMapping("/update/{careerId}")
    @Operation(summary = "Update Careers")
    @PreAuthorize("hasAnyAuthority('CAREER:UPDATE')")
    public ResponseEntity<Response<IDResponse<Long>>> update(@PathVariable Long careerId,
                                                             @RequestBody CareerRequest request) {
        request.setId(careerId);
        return ResponseUtils.success(careerService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }
}
