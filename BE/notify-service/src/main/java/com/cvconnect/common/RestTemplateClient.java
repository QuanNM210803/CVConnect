package com.cvconnect.common;

import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RestTemplateClient {
    @Autowired
    private RestTemplateService restTemplateService;

    @Value("${server.core_service}")
    private String SERVER_CORE_SERVICE;
    @Value("${server.user_service}")
    private String SERVER_USER_SERVICE;

    public String previewEmailDefault(String template, List<String> placeholders) {
        Map<String, Object> body = Map.of(
                "template", template,
                "placeholders", placeholders
        );
        Response<String> response = restTemplateService.postMethodRestTemplate(
                SERVER_CORE_SERVICE + "/replace-placeholder/internal/preview-email-default",
                new ParameterizedTypeReference<Response<String>>() {},
                body
        );
        return response.getData();
    }
}
