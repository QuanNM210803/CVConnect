package com.cvconnect.dto.jobAd;

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
public class JobAdProcessDto extends BaseDto<Instant> {
    private String name;
    private Integer sortOrder;
    private Long jobAdId;
    private Long processTypeId;
}
