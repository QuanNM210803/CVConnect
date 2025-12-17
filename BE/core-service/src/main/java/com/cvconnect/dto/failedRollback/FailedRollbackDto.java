package com.cvconnect.dto.failedRollback;

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
public class FailedRollbackDto extends BaseDto<Instant> {
    private String type;

    private String payload;

    private String errorMessage;

    private Boolean status;

    private Integer retryCount;
}
