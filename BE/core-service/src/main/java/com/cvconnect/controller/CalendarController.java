package com.cvconnect.controller;

import com.cvconnect.dto.calendar.CalendarRequest;
import com.cvconnect.service.CalendarService;
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
@RequestMapping("calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @Operation(summary = "Create Calendar", description = "Create a new calendar entry")
    @PreAuthorize("hasAnyAuthority('ORG_CALENDAR:ADD')")
    public ResponseEntity<Response<IDResponse<Long>>> createCalendar(@Valid @RequestBody CalendarRequest request) {
        return ResponseUtils.success(calendarService.createCalendar(request), localizationUtils.getLocalizedMessage(MessageConstants.CREATE_SUCCESSFULLY));
    }
}
