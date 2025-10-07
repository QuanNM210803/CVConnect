package com.cvconnect.dto.position;

import com.cvconnect.dto.positionLevel.PositionLevelDto;
import com.cvconnect.dto.positionProcess.PositionProcessDto;
import com.cvconnect.dto.department.DepartmentDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionDto extends BaseDto<Instant> {
    private String code;
    private String name;
    private Long departmentId;

    // attribute expansion
    private String departmentName;
    private String departmentCode;

    private List<PositionProcessDto> listProcess;
    private List<PositionLevelDto> listLevel;
    private DepartmentDto department;

    public PositionDto(Long id, String code, String name, Boolean isActive, String createdBy, String updatedBy, Instant createdAt, Instant updatedAt,
                       Long departmentId, String departmentName, String departmentCode) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
    }
}
