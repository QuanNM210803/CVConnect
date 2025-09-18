package com.cvconnect.controller;

import com.cvconnect.dto.level.LevelDto;
import com.cvconnect.dto.level.LevelFilterRequest;
import com.cvconnect.dto.level.LevelRequest;
import com.cvconnect.service.LevelService;
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
@RequestMapping("/level")
public class LevelController {
    @Autowired
    private LevelService levelService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/detail/{id}")
    @Operation(summary = "Get level detail by id")
    @PreAuthorize("hasAnyAuthority('LEVEL:VIEW')")
    public ResponseEntity<Response<LevelDto>> getDetailById(@PathVariable Long id) {
        return ResponseUtils.success(levelService.getById(id));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter levels")
    @PreAuthorize("hasAnyAuthority('LEVEL:VIEW')")
    public ResponseEntity<Response<FilterResponse<LevelDto>>> filter(@Valid @ModelAttribute LevelFilterRequest request) {
        return ResponseUtils.success(levelService.filter(request));
    }

    @PostMapping("/create")
    @Operation(summary = "Create new level")
    @PreAuthorize("hasAnyAuthority('LEVEL:ADD')")
    public ResponseEntity<Response<IDResponse<Long>>> create(@RequestBody @Valid LevelRequest request) {
        return ResponseUtils.success(levelService.create(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update level by id")
    @PreAuthorize("hasAnyAuthority('LEVEL:UPDATE')")
    public ResponseEntity<Response<IDResponse<Long>>> update(@PathVariable Long id, @RequestBody @Valid LevelRequest request) {
        request.setId(id);
        return ResponseUtils.success(levelService.update(request), localizationUtils.getLocalizedMessage(MessageConstants.UPDATE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete levels by ids")
    @PreAuthorize("hasAnyAuthority('LEVEL:DELETE')")
    public ResponseEntity<Response<Void>> delete(@RequestBody List<Long> ids) {
        levelService.deleteByIds(ids);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }
}
