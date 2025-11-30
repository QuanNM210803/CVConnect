package com.cvconnect.dto.jobAd;

import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.org.OrgDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdDto extends BaseDto<Instant> {
    private String code;

    private String title;

    private Long orgId;

    private Long positionId;

    private String jobType;

    private Instant dueDate;

    private Integer quantity;

    private String salaryType;

    private Integer salaryFrom;

    private Integer salaryTo;

    private String currencyType;

    private String keyword;

    private String description;

    private String requirement;

    private String benefit;

    private Long hrContactId;

    private String jobAdStatus;

    private Boolean isPublic;

    private Boolean isAutoSendEmail;

    private Long emailTemplateId;

    private Boolean isRemote;

    private Boolean isAllLevel;

    private String keyCodeInternal;

    // attribute expansion
    private String hrContactName;

    private String positionName;
    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    private UserDto hrContact;
    private OrgDto org;
    private Long numberOfApplications;
    private Long numberOfViews;
}
