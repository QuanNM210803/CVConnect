package com.cvconnect.controller;

import com.cvconnect.dto.position.PositionDto;
import com.cvconnect.dto.position.PositionFilterRequest;
import com.cvconnect.dto.position.PositionRequest;
import com.cvconnect.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.request.ChangeStatusActiveRequest;
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
@RequestMapping("/position")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('POSITION:ADD')")
    @Operation(summary = "Create positions")
    public ResponseEntity<Response<IDResponse<Long>>> createPositions(@Valid @RequestBody PositionRequest request) {
        return ResponseUtils.success(positionService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PutMapping("/change-status-active")
    @PreAuthorize("hasAnyAuthority('POSITION:UPDATE')")
    @Operation(summary = "Change status active positions")
    public ResponseEntity<Response<Void>> changeStatusActive(@RequestBody ChangeStatusActiveRequest request) {
        positionService.changeStatusActive(request);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('POSITION:DELETE')")
    @Operation(summary = "Delete positions")
    public ResponseEntity<Response<Void>> deletePositions(@RequestBody List<Long> ids) {
        positionService.deleteByIds(ids);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('POSITION:VIEW')")
    @Operation(summary = "Detail positions")
    public ResponseEntity<Response<PositionDto>> detailPositions(@PathVariable Long id) {
        return ResponseUtils.success(positionService.detail(id));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('POSITION:VIEW')")
    @Operation(summary = "Filter positions")
    public ResponseEntity<Response<FilterResponse<PositionDto>>> filterPositions(@Valid @ModelAttribute PositionFilterRequest request) {
        return ResponseUtils.success(positionService.filter(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('POSITION:UPDATE')")
    @Operation(summary = "Update positions")
    public ResponseEntity<Response<IDResponse<Long>>> updatePositions(@PathVariable Long id, @Valid @RequestBody PositionRequest request) {
        request.setId(id);
        return ResponseUtils.success(positionService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }
}
