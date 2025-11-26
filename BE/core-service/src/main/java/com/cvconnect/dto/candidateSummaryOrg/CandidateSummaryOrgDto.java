package com.cvconnect.dto.candidateSummaryOrg;

import com.cvconnect.dto.level.LevelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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
public class CandidateSummaryOrgDto extends BaseDto<Instant> {
    private String skill;

    private Long levelId;

    private Long orgId;

    private Long candidateInfoId;

    // attributes
    private LevelDto level;
}

