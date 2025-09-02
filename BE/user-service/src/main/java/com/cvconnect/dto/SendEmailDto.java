package com.cvconnect.dto;

import com.cvconnect.enums.EmailTemplateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendEmailDto {
    private String sender;
    private List<String> recipients;
    private List<String> ccList;
    private String subject;

    // For plain text email
    private String body;
    private Long orgId;

    // For template email
    private EmailTemplateEnum template;
    private Map<String, String> templateVariables = new HashMap<>();
}
