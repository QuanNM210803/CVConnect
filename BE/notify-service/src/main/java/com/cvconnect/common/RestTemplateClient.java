package com.cvconnect.common;

import com.cvconnect.dto.internal.request.DataReplacePlaceholder;
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

    public String previewEmail(String template, List<String> placeholders, DataReplacePlaceholder dataReplacePlaceholder, Boolean isDefault) {
        Map<String, Object> body = Map.of(
                "template", template,
                "placeholders", placeholders,
                "baseData", dataReplacePlaceholder,
                "isDefault", isDefault
        );
        Response<String> response = restTemplateService.postMethodRestTemplate(
                SERVER_CORE_SERVICE + "/replace-placeholder/internal/preview-email",
                new ParameterizedTypeReference<Response<String>>() {},
                body
        );
        return response.getData();
    }

    public Long validOrgMember() {
        Response<Long> response = restTemplateService.getMethodRestTemplate(
                SERVER_USER_SERVICE + "/org-member/internal/valid-org-member",
                new ParameterizedTypeReference<Response<Long>>() {}
        );
        return response.getData();
    }
}
