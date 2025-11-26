package com.cvconnect.controller;

import com.cvconnect.dto.searchHistoryOutside.SearchHistoryOutsideDto;
import com.cvconnect.service.SearchHistoryOutsideService;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.constant.MessageConstants;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.LocalizationUtils;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search-history-outside")
public class SearchHistoryOutsideController {
    @Autowired
    private SearchHistoryOutsideService searchHistoryOutsideService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @GetMapping("/my-search-history")
    @Operation(summary = "Get My Search History Outside")
    public ResponseEntity<Response<List<SearchHistoryOutsideDto>>> getMySearchHistoryOutside() {
        return ResponseUtils.success(searchHistoryOutsideService.getMySearchHistoryOutside());
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete My Search History Outside")
    public ResponseEntity<Response<Void>> deleteMySearchHistoryOutside(@RequestBody List<Long> ids) {
        searchHistoryOutsideService.deleteByIds(ids);
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }

    @DeleteMapping("/delete-all")
    @Operation(summary = "Delete All My Search History Outside")
    public ResponseEntity<Response<Void>> deleteAllMySearchHistoryOutside() {
        searchHistoryOutsideService.deleteAllByUserId();
        return ResponseUtils.success(null, localizationUtils.getLocalizedMessage(MessageConstants.DELETE_SUCCESSFULLY));
    }
}
