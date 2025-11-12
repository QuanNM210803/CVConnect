package com.cvconnect.dto.jobAd;

import com.cvconnect.dto.career.CareerDto;
import com.cvconnect.dto.enums.JobTypeDto;
import com.cvconnect.dto.level.LevelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class JobAdOutsideDataFilter {
    private List<CareerDto> careers;
    private List<LevelDto> levels;
    private List<JobTypeDto> jobTypes;
}
