package com.cvconnect.dto.jobAd;

import com.cvconnect.dto.department.DepartmentDto;
import com.cvconnect.dto.enums.JobAdStatusDto;
import com.cvconnect.dto.internal.response.UserDto;
import com.cvconnect.dto.position.PositionDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobAdOrgDetailResponse {
    private Long id;
    private String title;
    private PositionDto position;
    private DepartmentDto department;
    private Instant dueDate;
    private Integer quantity;
    private UserDto hrContact;
    private JobAdStatusDto jobAdStatus;
    private Boolean isPublic;
    private String keyCodeInternal;

    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

    private List<JobAdProcessDto> jobAdProcess;
}
