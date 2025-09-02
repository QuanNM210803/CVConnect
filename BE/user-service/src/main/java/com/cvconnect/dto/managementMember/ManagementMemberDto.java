package com.cvconnect.dto.managementMember;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagementMemberDto {
    private Long id;

    private Long userId;
    private Boolean isActive;
    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}
