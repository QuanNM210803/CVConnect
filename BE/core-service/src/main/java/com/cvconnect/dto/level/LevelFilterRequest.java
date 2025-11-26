package com.cvconnect.dto.level;

import lombok.Data;
import nmquan.commonlib.dto.request.FilterRequest;

import java.time.Instant;

@Data
public class LevelFilterRequest extends FilterRequest {
    private String code;
    private String name;

    private Instant createdAtStart;
    private Instant createdAtEnd;
    private Instant updatedAtStart;
    private Instant updatedAtEnd;
    private String createdBy;
    private String updatedBy;
}
