package com.cvconnect.controller;

import com.cvconnect.common.ReplacePlaceholder;
import io.swagger.v3.oas.annotations.Operation;
import nmquan.commonlib.annotation.InternalRequest;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/replace-placeholder")
public class ReplacePlaceholderController {
    @Autowired
    private ReplacePlaceholder replacePlaceholder;

    @InternalRequest
    @PostMapping("/internal/preview-email-default")
    @Operation(summary = "Preview email content for job application")
    public ResponseEntity<Response<String>> previewEmailDefault(@RequestBody Map<String, Object> body) {
        String template = (String) body.get("template");
        List<String> placeholders = (List<String>) body.get("placeholders");
        return ResponseUtils.success(replacePlaceholder.previewEmailDefault(template, placeholders));
    }

}
