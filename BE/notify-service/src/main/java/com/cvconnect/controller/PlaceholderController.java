package com.cvconnect.controller;

import com.cvconnect.dto.PlaceholderDto;
import com.cvconnect.service.PlaceholderService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/placeholder")
public class PlaceholderController {
    @Autowired
    private PlaceholderService placeholderService;

    @GetMapping("/filter")
    @Operation(summary = "Filter Placeholders")
    public ResponseEntity<Response<List<PlaceholderDto>>> filterPlaceholders() {
        return ResponseUtils.success(placeholderService.filter());
    }

}
