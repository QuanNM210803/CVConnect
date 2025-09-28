package com.cvconnect.dto;

import com.cvconnect.dto.common.Identifiable;
import com.cvconnect.dto.processType.ProcessTypeDto;
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
public class PositionProcessDto extends BaseDto<Instant> implements Identifiable {
    private String name;
    private Long positionId;
    private Long processTypeId;
    private Integer sortOrder;

    // attribute expansion
    private ProcessTypeDto processType;
}
