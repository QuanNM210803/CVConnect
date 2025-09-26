package com.cvconnect.controller;

import com.cvconnect.dto.position.PositionRequest;
import com.cvconnect.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.IDResponse;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
