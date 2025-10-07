package com.cvconnect.dto.positionLevel;

import com.cvconnect.dto.level.LevelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.Identifiable;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionLevelDto extends BaseDto<Instant> implements Identifiable {
    private String name;
    private Long positionId;
    private Long levelId;

    // attribute expansion
    private LevelDto level;
}
