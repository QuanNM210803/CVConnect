package com.cvconnect.dto;


import com.cvconnect.enums.EmailTemplateEnum;
import com.cvconnect.enums.SendEmailStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailLogDto extends BaseDto<Instant> {
    private Long id;
    private String messageId;
    private String replyMessageId;
    private String emailGroup;
    private String sender;
    private String recipients;
    private String ccList;
    private String subject;
    private String body;
    private Long emailTemplateId;
    private EmailTemplateEnum template;
    private String templateVariables;
    private SendEmailStatus status;
    private String errorMessage;
    private Instant sentAt;

    private List<String> recipientList;
    private List<String> ccListArray;
}
