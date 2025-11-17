package com.cvconnect.dto.processType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessTypeDto extends BaseDto<Instant> {
    private String code;
    private String name;
    private Integer sortOrder;
    private Boolean isDefault;

    // add
    private Instant transferDate;

}
