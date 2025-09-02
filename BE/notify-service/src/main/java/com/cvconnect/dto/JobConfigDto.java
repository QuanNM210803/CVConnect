package com.cvconnect.dto;

import com.cvconnect.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobConfigDto extends BaseDto<Instant> {
    private Long id;
    private String jobName;
    private ScheduleType scheduleType;
    private String description;
    private String expression;
}
