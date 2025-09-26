package com.cvconnect.dto.position;

import com.cvconnect.constant.Messages;
import com.cvconnect.dto.PositionLevelRequest;
import com.cvconnect.dto.PositionProcessRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionRequest {
    private Long id;

    @NotBlank(message = Messages.POSITION_NAME_REQUIRED)
    private String name;
    @NotBlank(message = Messages.POSITION_CODE_REQUIRED)
    private String code;
    @NotNull(message = Messages.DEPARTMENT_REQUIRED)
    private Long departmentId;

    @Valid
    private List<PositionLevelRequest> positionLevel;
    @Valid
    private List<PositionProcessRequest> positionProcess;

}
